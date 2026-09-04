package br.com.onebr.config;

import br.com.onebr.service.AnalyticsService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Counts page views by treating each successful {@code GET /analytics/usersTotalCount}
 * call as one visit to the {@code path} it asks about. The increment runs in
 * {@link #afterCompletion} — after the response is flushed — so it adds no latency
 * and only counts requests that were actually served.
 */
@Component
@Slf4j
public class PageAccessInterceptor implements HandlerInterceptor {

    static final String COUNTED_PATH = "/analytics/usersTotalCount";

    @Autowired
    private AnalyticsService analyticsService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex != null || !HttpMethod.GET.matches(request.getMethod())) {
            return;
        }
        if (response.getStatus() < 200 || response.getStatus() >= 300) {
            return;
        }
        if (!request.getRequestURI().endsWith(COUNTED_PATH)) {
            return;
        }
        final String path = request.getParameter("path");
        try {
            analyticsService.registerHit(path);
        } catch (Exception e) {
            log.warn("message=Failed to register page access. path={} error={}", path, e.getMessage());
        }
    }
}
