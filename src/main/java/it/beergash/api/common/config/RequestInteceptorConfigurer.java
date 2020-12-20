package it.beergash.api.common.config;

import it.beergash.api.common.handler.LoggerRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RequestInteceptorConfigurer implements WebMvcConfigurer {

    @Bean
    public LoggerRequestHandler loggingReqInterceptor() {
        return new LoggerRequestHandler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingReqInterceptor());
    }
}

