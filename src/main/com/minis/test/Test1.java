package com.minis.test;

import com.minis.beans.ClassPathXmlApplicationContext;
import com.minis.beans.BeansException;

public class Test1 {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext ctx = new
                ClassPathXmlApplicationContext("com/minis/test/beans.xml");
        BaseService baseservice = (BaseService)ctx.getBean("baseservice");

        baseservice.getBbs().getAs().sayHello();
    }
}