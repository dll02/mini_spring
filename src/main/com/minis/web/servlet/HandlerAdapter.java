package com.minis.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}