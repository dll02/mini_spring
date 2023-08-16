package com.minis.aop;

public class AfterReturningAdviceInterceptor implements MethodInterceptor, AfterAdvice {

    private final AfterReturningAdvice advice;

    public AfterReturningAdviceInterceptor(AfterReturningAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        // afterReturning 它内部传入了返回参数，说明是目标方法执行返回后，再调用该方法，在方法里面可以拿到返回的参数
        this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
        return retVal;
    }

}
