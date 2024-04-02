package cn.micro.unique.filter;

import cn.micro.unique.util.TraceIdUtil;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.Ordered;
import org.springframework.util.Base64Utils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

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
            String traceId = Optional.ofNullable(MDC.get(requestKey))
                    .orElse(Optional.ofNullable(request.getHeader(requestKey))
                            .orElse(Optional.ofNullable(traceIdSw8Header(request.getHeader("sw8")))
                                    .orElse(TraceIdUtil.genTraceId())));
            MDC.put(requestKey, traceId);
            filterChain.doFilter(request, response);
            response.setHeader(requestKey, traceId);
        } finally {
            MDC.remove(requestKey);
        }
    }

    /**
     * SkyWalking sw8获取跟踪ID
     *
     * @param sw8 header 值
     * @return 跟踪ID
     */
    private String traceIdSw8Header(String sw8) {
        try {
            String[] values = sw8.split("-");
            String value = values[1];
            return new String(Base64Utils.decodeFromString(value));
        } catch (Exception ex) {
            return null;
        }
    }

}
