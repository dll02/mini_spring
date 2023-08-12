package com.minis.test.mvc;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.test.auto.BaseService;
import com.minis.web.RequestMapping;

public class HelloBean {
    @Autowired
    BaseService baseService;

    @RequestMapping("/test")
    public String doTest(){
        return baseService.sayHello();
//        return "hello world for doGet!";
    }
}
