package com.minis.aop.framework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.minis.aop.*;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    Object target;
    Advisor advisor;  //should be a list to support multiple advisors

    public JdkDynamicAopProxy(Object target, Advisor advisor) {
        this.target = target;
        this.advisor = advisor;
    }

    public JdkDynamicAopProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object getProxy() {
        Object obj;
        obj = Proxy.newProxyInstance(JdkDynamicAopProxy.class.getClassLoader(),
                target.getClass().getInterfaces(), this);
        return obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> targetClass = (target != null ? target.getClass() : null);
//        if (method.getName().equals("doAction")) {
//
//            MethodInterceptor interceptor = this.advisor.getMethodInterceptor();
//            MethodInvocation invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass);
//            return interceptor.invoke(invocation);
//        }
//        return null;

        if (this.advisor instanceof PointcutAdvisor) {
            if (((PointcutAdvisor) this.advisor).getPointcut().getMethodMatcher().matches(method, targetClass)) {
                MethodInterceptor interceptor = this.advisor.getMethodInterceptor();
                MethodInvocation invocation =
                        new ReflectiveMethodInvocation(proxy, target, method, args, targetClass);
                return interceptor.invoke(invocation);
            }
        }
//        if (this.advisor instanceof AsyncAnnotationAdvisor) {
//            MethodInterceptor interceptor = this.advisor.getMethodInterceptor();
//
//            MethodInvocation invocation =
//                    new ReflectiveMethodInvocation(proxy, target, method, args, targetClass);
//            return interceptor.invoke(invocation);
//        }
        return null;
    }
}
