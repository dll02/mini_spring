package com.minis.test.auto;


public class BaseBaseService {
    private AServiceImpl as;

    public AServiceImpl getAs() {
        return as;
    }

    public void setAs(AServiceImpl as) {
        this.as = as;
    }

    public String sayHello() {
        return as.sayHello();
    }
}