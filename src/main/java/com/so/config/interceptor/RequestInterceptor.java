package com.so.config.interceptor;


import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
//        long startTime = System.currentTimeMillis();
//        System.out.println("\n-------- RequestInterceptor.preHandle --- ");
//        System.out.println("Request URL: " + getFullURL(request));
//        System.out.println("Start Time: " + System.currentTimeMillis());
//        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, //
                           Object handler, ModelAndView modelAndView) throws Exception {
//        System.out.println("\n-------- RequestInterceptor.postHandle --- ");
//        System.out.println("Request URL: " + getFullURL(request));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, //
                                Object handler, Exception ex) throws Exception {
//        System.out.println("\n-------- RequestInterceptor.afterCompletion --- ");
//        long startTime = (Long) request.getAttribute("startTime");
//        long endTime = System.currentTimeMillis();
//        System.out.println("Request URL: " + getFullURL(request));
//        System.out.println("End Time: " + endTime);
//        System.out.println("Time Taken: " + (endTime - startTime));
    }

    public static String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();
        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }
}