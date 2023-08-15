package com.minis.test.mvc.controller;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.test.mvc.entity.User;
import com.minis.test.mvc.service.BaseService;
import com.minis.test.mvc.service.IAction;
import com.minis.test.mvc.service.UserService;
import com.minis.web.bind.annotation.RequestMapping;
import com.minis.web.bind.annotation.ResponseBody;
import com.minis.web.servlet.ModelAndView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class HelloController {

    @Autowired
    UserService userService;

    @RequestMapping("/test2")
    public void doTest2(HttpServletRequest request, HttpServletResponse response) {
        String str = "test 2, hello world!";
        try {
            response.getWriter().write(str);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @RequestMapping("/test4")
    public ModelAndView doTest4() {
        User user = userService.getUserInfo(1);
        ModelAndView mav = new ModelAndView("test", "msg", user.toString());
        return mav;

    }

    @RequestMapping("/test5")
    public ModelAndView doTest5(User user) {

        ModelAndView mav = new ModelAndView("test", "msg", user.getName());
        return mav;
    }

    @RequestMapping("/test6")
    public String doTest6(User user) {
        return "error";
    }

    @RequestMapping("/test7")
    @ResponseBody
    public User doTest7(User user) {
        user.setName(user.getName() + "---");
        user.setBirthday(new Date());
        return user;
    }

}
