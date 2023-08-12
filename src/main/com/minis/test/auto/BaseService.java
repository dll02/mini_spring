package com.minis.test.auto;


import com.minis.beans.factory.annotation.Autowired;

public class BaseService {
    @Autowired
    private BaseBaseService bbs;

    public BaseBaseService getBbs() {
        return bbs;
    }

    public void setBbs(BaseBaseService bbs) {
        this.bbs = bbs;
    }

    public BaseService() {
    }

    public String sayHello() {
        System.out.println("Base Service says Hello");
        return bbs.sayHello();
    }
}
