#!/bin/sh
# onebr-access-stats.sh
# ---------------------------------------------------------------------------
# Extrai estatisticas de acesso ao site OneBR a partir dos logs do nginx da
# maquina de producao, para servir de baseline ao contador local que substitui
# a integracao (morta) com o Universal Analytics.
#
# USO:   sudo sh onebr-access-stats.sh [dir-de-logs] [data-inicio-do-site]
#   dir-de-logs           default: /var/log/nginx
#   data-inicio-do-site   default: 2020-10-01  (1a captura no web.archive.org;
#                         o commit da integracao GA e de 2020-12-05)
#
# Precisa rodar como root (sudo) porque /var/log/nginx normalmente e 0700.
# Nao escreve nada, so le os logs e imprime na tela. Cole a saida inteira.
# ---------------------------------------------------------------------------

set -u

LOG_DIR="${1:-/var/log/nginx}"
SITE_START="${2:-2020-10-01}"

if [ ! -d "$LOG_DIR" ]; then
    echo "ERRO: diretorio de logs nao encontrado: $LOG_DIR" >&2
    exit 1
fi

# access.log* rotacionados + o atual. -f no zcat deixa passar arquivos texto.
FILES=$(ls -1 "$LOG_DIR"/access.log-* "$LOG_DIR"/access.log 2>/dev/null)
if [ -z "$FILES" ]; then
    echo "ERRO: nenhum access.log em $LOG_DIR (rodou com sudo?)" >&2
    exit 1
fi

CAT="zcat -f"
command -v zcat >/dev/null 2>&1 || CAT="gunzip -c -f"

echo "=========================================================================="
echo " OneBR - estatisticas de acesso (nginx: $LOG_DIR)"
echo " gerado em: $(date -u '+%Y-%m-%d %H:%M:%SZ')"
echo "=========================================================================="
echo
echo "### Arquivos analisados:"
echo "$FILES" | sed 's/^/  /'
echo

echo "### Retencao configurada no logrotate (quantos dias de historico existem):"
grep -hE 'rotate|daily|weekly|dateext|maxage' /etc/logrotate.d/nginx /etc/logrotate.conf 2>/dev/null | sed 's/^/  /' || echo "  (config nao encontrada)"
echo

echo "### 3 linhas de amostra (formato do log):"
$CAT $FILES 2>/dev/null | head -3 | sed 's/^/  /'
echo

echo "### log_format / access_log definidos no nginx:"
grep -rhE 'log_format|access_log' /etc/nginx/ 2>/dev/null | sed 's/^/  /' || echo "  (nao encontrado)"
echo

echo "### Total de requests registrados (todos os arquivos):"
$CAT $FILES 2>/dev/null | wc -l
echo

# ---------------------------------------------------------------------------
# Analise principal via awk. Formato "combined" do nginx:
#   $1=ip  $4=[dd/Mon/yyyy:HH:MM:SS  $6="METHOD  $7=path  $9=status
# ---------------------------------------------------------------------------
$CAT $FILES 2>/dev/null | awk '
BEGIN {
    split("Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec", mn, " ")
    for (i = 1; i <= 12; i++) m2n[mn[i]] = sprintf("%02d", i)
}
{
    # data: $4 = [04/Sep/2026:00:21:15
    ts = $4; gsub(/\[/, "", ts)
    split(ts, a, ":"); dmy = a[1]                 # 04/Sep/2026
    split(dmy, d, "/")
    day   = d[3] "-" m2n[d[2]] "-" d[1]           # 2026-09-04
    month = d[3] "-" m2n[d[2]]                    # 2026-09
    if (day !~ /^[0-9]/) next

    method = $6; gsub(/"/, "", method)
    path   = $7
    status = $9
    ip     = $1

    total_day[day]++
    total_month[month]++

    # "page view" = GET de rota da SPA (nao asset, nao /api, nao /images),
    # resposta 200/304. Isso conta entradas/reloads; navegacao client-side
    # do Vue NAO passa pelo nginx, entao e um piso do numero real de views.
    is_asset = (path ~ /\.(js|css|png|jpe?g|gif|svg|ico|woff2?|ttf|eot|map|json|txt|xml)($|\?)/)
    if (method == "GET" && !is_asset && path !~ /^\/api/ && path !~ /^\/images/ \
        && (status == 200 || status == 304)) {
        pv_day[day]++
        pv_month[month]++
        pv_total++
    }

    # visitantes unicos aproximados: par (dia, ip). Atras do Cloudflare o $1
    # pode ser o IP da borda -> tratar como aproximacao grosseira.
    key = day SUBSEP ip
    if (!(key in seen)) { seen[key] = 1; uniq_day[day]++; uniq_month[month]++ }

    # top paths (so rotas de conteudo)
    if (method == "GET" && !is_asset && path !~ /^\/api/ && path !~ /^\/images/)
        pathcount[path]++
}
END {
    n = asorti(total_day, sd)
    if (n > 0) print "### Janela coberta: " sd[1] " -> " sd[n] "  (" n " dias)\n"

    print "### Requests por dia:"
    for (i = 1; i <= n; i++) printf "  %-12s %8d\n", sd[i], total_day[sd[i]]
    print ""

    print "### Page views (rotas SPA, 200/304) por dia:"
    n = asorti(pv_day, sd)
    for (i = 1; i <= n; i++) printf "  %-12s %8d\n", sd[i], pv_day[sd[i]]
    print ""

    print "### Page views por mes:"
    n = asorti(pv_month, sm)
    for (i = 1; i <= n; i++) printf "  %-9s %10d\n", sm[i], pv_month[sm[i]]
    print ""

    print "### Visitantes unicos aprox. (dia,IP distinto) por dia:"
    n = asorti(uniq_day, sd)
    for (i = 1; i <= n; i++) printf "  %-12s %8d\n", sd[i], uniq_day[sd[i]]
    print ""

    # media diaria de page views usando apenas dias "cheios"
    # (descarta 1o e ultimo dia, que sao parciais)
    n = asorti(pv_day, sd)
    sum = 0; cnt = 0
    for (i = 2; i < n; i++) { sum += pv_day[sd[i]]; cnt++ }
    if (cnt < 1) { for (i = 1; i <= n; i++) { sum += pv_day[sd[i]]; cnt++ } }
    avg = (cnt > 0) ? sum / cnt : 0
    printf "### Media de page views/dia (dias completos, n=%d): %.0f\n", cnt, avg
    print ""

    print "### Top 25 rotas de conteudo:"
    n = asorti(pathcount, sp, "@val_num_desc")
    for (i = 1; i <= n && i <= 25; i++) printf "  %8d  %s\n", pathcount[sp[i]], sp[i]
    print ""

    print "AVG_PV_DAY=" avg
    print "PV_TOTAL_WINDOW=" pv_total
}
' > /tmp/onebr_stats_$$.txt 2>/dev/null

cat /tmp/onebr_stats_$$.txt | grep -v '^AVG_PV_DAY=\|^PV_TOTAL_WINDOW='

AVG=$(grep '^AVG_PV_DAY=' /tmp/onebr_stats_$$.txt | cut -d= -f2)
rm -f /tmp/onebr_stats_$$.txt

# ---------------------------------------------------------------------------
# Extrapolacao para baseline
# ---------------------------------------------------------------------------
echo "=========================================================================="
echo " ESTIMATIVA DE BASELINE (para o INSERT no V43)"
echo "=========================================================================="
if [ -z "${AVG:-}" ] || [ "$AVG" = "0" ]; then
    echo "  Nao foi possivel calcular a media. Verifique o formato do log acima."
else
    NOW_EPOCH=$(date -u +%s)
    START_EPOCH=$(date -u -d "$SITE_START" +%s 2>/dev/null || date -u -j -f "%Y-%m-%d" "$SITE_START" +%s 2>/dev/null)
    if [ -n "${START_EPOCH:-}" ]; then
        DAYS=$(( (NOW_EPOCH - START_EPOCH) / 86400 ))
        EST=$(awk -v a="$AVG" -v d="$DAYS" 'BEGIN { printf "%.0f", a * d }')
        echo "  media page views/dia (amostra atual) : $AVG"
        echo "  inicio do site assumido              : $SITE_START"
        echo "  dias desde o inicio                  : $DAYS"
        echo
        echo "  >>> baseline estimado ~ $EST page views  (media_atual * dias)"
        echo
        echo "  ATENCAO: e uma extrapolacao linear grosseira. O trafego dos"
        echo "  primeiros anos provavelmente foi menor; trate como ordem de"
        echo "  grandeza. Ajuste SITE_START ou aplique um fator se tiver"
        echo "  qualquer print antigo do painel do Universal Analytics."
        echo
        echo "  Linha para a migration V43 (ajuste o path/valor):"
        echo "    INSERT INTO page_access (path, hits) VALUES ('/', $EST);"
    else
        echo "  (nao consegui converter SITE_START=$SITE_START para epoch)"
    fi
fi
echo
echo "### DICA: aumentar a retencao dos logs para o futuro"
echo "  edite /etc/logrotate.d/nginx  ->  rotate 90  + dateext"
echo "=========================================================================="
