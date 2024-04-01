package cn.micro.unique;

import cn.micro.unique.filter.RequestUniqueFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class UniqueAutoConfiguration {

    @Bean
    @ConditionalOnBean(DispatcherServlet.class)
    public RequestUniqueFilter requestUniqueFilter() {
        return new RequestUniqueFilter();
    }

}
