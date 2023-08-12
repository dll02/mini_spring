package com.minis.web.servlet;

import com.minis.web.context.WebApplicationContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class RequestMappingHandlerAdapter implements HandlerAdapter {
    WebApplicationContext wac;

    public RequestMappingHandlerAdapter(WebApplicationContext wac) {
        this.wac = wac;
    }

    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        handleInternal(request, response, (HandlerMethod) handler);
    }
    private void handleInternal(HttpServletRequest request, HttpServletResponse response,
                                HandlerMethod handler) {
        Method method = handler.getMethod();
        Object obj = handler.getBean();
        Object objResult = null;
        try {
            objResult = method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            response.getWriter().append(objResult.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}