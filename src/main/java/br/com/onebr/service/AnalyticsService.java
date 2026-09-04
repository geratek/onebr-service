package br.com.onebr.service;

import br.com.onebr.controller.AnalyticsRes;
import br.com.onebr.repository.PageAccessRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Page-view counter backed by the local {@code page_access} table.
 *
 * <p>Replaces the former Google Analytics (Universal Analytics, view 222212743)
 * integration, which stopped serving data after UA was shut down. The frontend
 * already calls {@code GET /analytics/usersTotalCount?path=<path>} on every page
 * load, so {@link br.com.onebr.config.PageAccessInterceptor} treats that request
 * as the page-view signal and increments the counter after the response is sent.
 */
@Service
@Slf4j
public class AnalyticsService {

    private static final int MAX_PATH_LENGTH = 500;

    @Autowired
    private PageAccessRepository pageAccessRepository;

    /**
     * Records one visit for the given path. Called from the interceptor, after
     * the response has been committed, so it never adds latency to the request.
     */
    @Transactional
    public void registerHit(String path) {
        final String normalized = normalize(path);
        if (normalized == null) {
            return;
        }
        pageAccessRepository.registerHit(normalized);
        log.info("message=Page access registered. path={}", normalized);
    }

    public AnalyticsRes getUsersTotalCount(String path) {
        final String normalized = normalize(path);
        final Long hits = normalized == null ? null : pageAccessRepository.findHitsByPath(normalized);

        return AnalyticsRes.builder()
            .count(hits == null ? 0 : Math.toIntExact(hits))
            .build();
    }

    private String normalize(String path) {
        if (!StringUtils.hasText(path)) {
            return null;
        }
        String normalized = path.trim();
        if (normalized.length() > 1 && normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        if (normalized.length() > MAX_PATH_LENGTH) {
            normalized = normalized.substring(0, MAX_PATH_LENGTH);
        }
        return normalized;
    }
}
