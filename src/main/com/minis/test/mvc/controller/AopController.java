package com.minis.test.mvc.controller;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.test.mvc.service.DynamicProxy;
import com.minis.test.mvc.service.IAction;
import com.minis.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public class AopController {

    @Autowired
    IAction action;

    @RequestMapping("/aop1")
    public void doTest1(HttpServletRequest request, HttpServletResponse response) {
        DynamicProxy proxy = new DynamicProxy(action);
        IAction p = (IAction) proxy.getProxy();
        p.doAction();
        String str = "test aop, hello world!";
        try {
            response.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/aop2")
    public void doTest2(HttpServletRequest request, HttpServletResponse response) {
        action.doAction();
        String str = "test aop, hello world!";
        try {
            response.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/aop3")
    public void doTest3(HttpServletRequest request, HttpServletResponse response) {
        action.doSomething();
    }

    @Autowired
    IAction action2;

    @RequestMapping("/aop4")
    public void doTest4(HttpServletRequest request, HttpServletResponse response) {
        action2.doAction();
    }

    @RequestMapping("/aop5")
    public void doTest5(HttpServletRequest request, HttpServletResponse response) {
        action2.doSomething();
    }
}
