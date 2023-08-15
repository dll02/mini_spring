package com.minis.web.servlet;


import com.minis.web.method.HandlerMethod;
import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    HandlerMethod getHandler(HttpServletRequest request) throws Exception;
}


