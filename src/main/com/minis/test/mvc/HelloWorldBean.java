package com.minis.test.mvc;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.test.auto.BaseService;
import com.minis.test.mvc.entity.User;
import com.minis.web.bind.annotation.RequestMapping;
import com.minis.web.bind.annotation.ResponseBody;

import java.util.Date;

public class HelloWorldBean {
    @Autowired
    BaseService baseService;

    @RequestMapping("/test")
    @ResponseBody
    public String doTest(){
        return baseService.sayHello();
//        return "hello world for doGet!";
    }

}
