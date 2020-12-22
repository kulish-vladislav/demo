package com.example.demo.config;

import com.example.demo.filter.LogWriter;
import com.example.demo.filter.RequestResponseLoggingFilter;
import com.example.demo.filter.SimpleLogWriter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Vladislav Kulish on 22.12.2020
 */
@Configuration
public class WebFilterConfig {

    @Bean
    @Primary
    public LogWriter simpleLogWriter() {
        return new SimpleLogWriter();
    }

    @Bean
    public FilterRegistrationBean<RequestResponseLoggingFilter> loggingFilter(LogWriter logWriter) {
        FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new RequestResponseLoggingFilter(logWriter));
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
