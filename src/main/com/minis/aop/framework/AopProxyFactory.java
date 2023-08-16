package com.minis.aop.framework;

import com.minis.aop.PointcutAdvisor;
import com.minis.aop.framework.AopProxy;

public interface AopProxyFactory {
    AopProxy createAopProxy(Object target, PointcutAdvisor adviseor);
}
