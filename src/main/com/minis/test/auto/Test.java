package com.minis.test.auto;

import com.minis.beans.BeansException;
import com.minis.beans.ClassPathXmlApplicationContext;

public class Test {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext ctx = new
                ClassPathXmlApplicationContext("com/minis/test/auto/beans.xml");
        BaseService baseservice = (BaseService)ctx.getBean("baseservice");

        baseservice.sayHello();
    }
}