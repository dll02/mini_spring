package com.minis.test.mvc;

import com.minis.web.RequestMapping;

public class HelloBean {

    @RequestMapping("/test")
    public String doTest(){
        return "hello world for doGet!";
    }
}
