package it.beergash.api.common.handler;

import it.beergash.api.common.annotations.RequestLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.BooleanUtils;


@Component
public class LoggerRequestHandler extends HandlerInterceptorAdapter {

    @Value("${trace.enabled}")
    private boolean traceEnabled;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.getMethod().isAnnotationPresent(RequestLogger.class) && BooleanUtils.toBoolean(traceEnabled)) {
                ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
                byte[] responseBytes = responseWrapper.getContentAsByteArray();
                String jsonResponse = new String(responseBytes, StandardCharsets.UTF_8);
                logger.info(String.format("Request INFO: " +
                                "Url [%s]; " +
                                "Method [%s]",
                                "Response Body [%s]",
                        request.getRequestURL().toString(), request.getMethod(), jsonResponse));
            }
        }
    }
}