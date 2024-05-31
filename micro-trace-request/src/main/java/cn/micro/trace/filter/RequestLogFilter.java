package cn.micro.trace.filter;

import cn.micro.trace.service.TraceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.UriUtils;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.micro.trace.constant.TraceConstant.REQUEST_SERVICE_METHOD;

/**
 * 请求拦截打印
 */
public class RequestLogFilter extends OncePerRequestFilter implements OrderedFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraceService.class);

    @Resource
    private TraceService traceService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String requestUrl = httpServletRequest.getRequestURL().toString();
        String queryString = httpServletRequest.getQueryString();
        if (!StringUtils.isEmpty(queryString)) {
            queryString = UriUtils.decode(queryString, httpServletRequest.getCharacterEncoding());
            requestUrl += "?" + queryString;
        }
        String method = httpServletRequest.getMethod();
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        Map<String, List<String>> headers = Collections.list(headerNames)
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        h -> Collections.list(httpServletRequest.getHeaders(h))
                ));
        Map<String, List<String>> parameters = httpServletRequest.getParameterMap()
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, value -> Arrays.asList(value.getValue())));
        ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(httpServletResponse);
        long startTime = System.nanoTime();
        try {
            filterChain.doFilter(contentCachingRequestWrapper, contentCachingResponseWrapper);
        } finally {
            ServletInputStream inputStream = contentCachingRequestWrapper.getInputStream();
            byte[] reqBody = inputStream.isFinished() ? contentCachingRequestWrapper.getContentAsByteArray() : StreamUtils.copyToByteArray(inputStream);
            byte[] rspBody = contentCachingResponseWrapper.getContentAsByteArray();
            if (!httpServletResponse.isCommitted()) {
                httpServletResponse.getOutputStream().write(rspBody);
            }
            try {
                traceService.request(requestUrl
                        , method
                        , headers
                        , parameters
                        , reqBody
                        , contentCachingResponseWrapper.getStatus()
                        , rspBody
                        , Optional.ofNullable(httpServletRequest.getAttribute(REQUEST_SERVICE_METHOD))
                                .map(Object::toString).orElse(null)
                        , (System.nanoTime() - startTime));
            } catch (Throwable throwable) {
                // 忽略异常，不处理
                LOGGER.error(throwable.getMessage(), throwable);
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String method = request.getMethod();
        if (HttpMethod.OPTIONS.matches(method)
                || HttpMethod.HEAD.matches(method)) {
            return true;
        }
        return super.shouldNotFilter(request);
    }

    @Override
    public int getOrder() {
        return (Ordered.HIGHEST_PRECEDENCE + 1000);
    }
}
