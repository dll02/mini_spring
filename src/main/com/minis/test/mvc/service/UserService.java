package com.minis.test.mvc.service;

import com.minis.jdbc.core.JdbcTemplate;
import com.minis.test.mvc.dao.UserJdbcImpl;
import com.minis.test.mvc.entity.User;



public class UserService {
    public User getUserInfo(int userid) {
        String sql = "select id, name,birthday from user where id=" + userid;
        JdbcTemplate jdbcTemplate = new UserJdbcImpl();
        User rtnUser = (User) jdbcTemplate.query(sql);
        return rtnUser;
    }
}
