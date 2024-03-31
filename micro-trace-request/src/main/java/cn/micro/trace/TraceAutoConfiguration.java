package cn.micro.trace;


import cn.micro.trace.aop.RequestAspect;
import cn.micro.trace.filter.RequestLogFilter;
import cn.micro.trace.service.TraceService;
import cn.micro.trace.service.impl.DefaultTraceServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class TraceAutoConfiguration {

    @Bean
    @ConditionalOnBean(DispatcherServlet.class)
    public RequestLogFilter requestLogFilter() {
        return new RequestLogFilter();
    }

    @Bean
    @ConditionalOnBean(DispatcherServlet.class)
    @ConditionalOnMissingBean
    public TraceService traceService() {
        return new DefaultTraceServiceImpl();
    }

    @Bean
    @ConditionalOnBean(DispatcherServlet.class)
    @ConditionalOnClass(RequestContextHolder.class)
    @ConditionalOnMissingBean
    public RequestAspect requestAspect() {
        return new RequestAspect();
    }

}
