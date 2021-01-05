package it.beergash.api.common.handler;

import it.beergash.api.common.annotations.RequestLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class LoggerRequestHandler extends HandlerInterceptorAdapter {

    @Value("${trace.enabled:false}")
    private boolean traceEnabled;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.getMethod().isAnnotationPresent(RequestLogger.class) && traceEnabled) {
                logger.info(String.format("Request PRE: " +
                                "Url [%s]; " +
                                "Method [%s]",
                        request.getRequestURL().toString(), request.getMethod()));
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.getMethod().isAnnotationPresent(RequestLogger.class) && traceEnabled) {
                logger.info(String.format("Request AFTER: " +
                                "Url [%s]; " +
                                "Method [%s]; " +
                                "Status [%d-%s] ",
                        request.getRequestURL().toString(), request.getMethod(), response.getStatus(), HttpStatus.valueOf(response.getStatus()).name()));
            }
        }
    }
}