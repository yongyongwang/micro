package cn.micro.unique.filter;

import cn.micro.unique.util.TraceIdUtil;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestUniqueFilter extends OncePerRequestFilter implements OrderedFilter {

    @Value("${request.unique.key:x-request-id}")
    private String requestKey;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String traceId = TraceIdUtil.genTraceId();
            MDC.put(requestKey, traceId);
            filterChain.doFilter(request, response);
            response.setHeader(requestKey, traceId);
        } finally {
            MDC.remove(requestKey);
        }
    }
}
