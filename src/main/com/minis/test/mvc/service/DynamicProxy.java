package com.minis.test.mvc.service;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
public class DynamicProxy {
    private Object subject = null;

    public DynamicProxy(Object subject) {
        this.subject = subject;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(DynamicProxy.class
                        .getClassLoader(), subject.getClass().getInterfaces(),
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getName().equals("doAction")) {
                            System.out.println("before call real object........");
                            return method.invoke(subject, args);
                        }
                        return null;
                    }
                });
    }
}