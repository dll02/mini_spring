package com.minis.beans.support;

import com.minis.beans.BeansException;

public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName) throws
            BeansException;
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}