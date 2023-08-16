package com.minis.test.mvc.service;

import com.minis.aop.MethodInterceptor;
import com.minis.aop.MethodInvocation;

public class TracingInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation i) throws Throwable {
        // 方法执行前后加逻辑
        System.out.println("method " + i.getMethod() + " is called on " + i.getThis() + " with args " + i.getArguments());
        Object ret = i.proceed();
        System.out.println("method " + i.getMethod() + " returns " + ret);
        return ret;
    }


}
