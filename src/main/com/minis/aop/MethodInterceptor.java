package com.minis.aop;

// 调用方法上的拦截器，也就是它实现在某个方法上的增强。
public interface MethodInterceptor extends Interceptor{
    Object invoke(MethodInvocation invocation) throws Throwable;
}
