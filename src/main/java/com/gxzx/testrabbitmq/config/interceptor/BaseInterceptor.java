package com.gxzx.testrabbitmq.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 拦截器基类
 * 
 */
public class BaseInterceptor extends HandlerInterceptorAdapter {
    protected static final Logger logger = LoggerFactory.getLogger(BaseInterceptor.class);
    private BaseInterceptor[] nextInterceptor;

    public void setNextInterceptor(BaseInterceptor... nextInterceptor) {
        this.nextInterceptor = nextInterceptor;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        if (nextInterceptor == null) {
            return true;
        }
        for (int i = 0; i < nextInterceptor.length; i++) {
            if (!nextInterceptor[i].preHandle(request, response, handler)) {
                return false;
            }
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        if (nextInterceptor != null) {
            for (int i = 0; i < nextInterceptor.length; i++) {
                nextInterceptor[i].postHandle(request, response, handler, modelAndView);
            }
        }
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
        if (nextInterceptor != null) {
            for (int i = 0; i < nextInterceptor.length; i++) {
                nextInterceptor[i].afterCompletion(request, response, handler, ex);
            }
        }
    }

    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        if (nextInterceptor != null) {
            for (int i = 0; i < nextInterceptor.length; i++) {
                nextInterceptor[i].afterConcurrentHandlingStarted(request, response, handler);
            }
        }
    }
}
