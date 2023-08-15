package com.minis.test.mvc.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import com.minis.jdbc.core.JdbcTemplate;
import com.minis.test.mvc.entity.User;

public class UserJdbcImpl extends JdbcTemplate {
//    @Override
    protected Object doInStatement(ResultSet rs) {
        //从jdbc数据集读取数据，并生成对象返回
        User rtnUser = null;
        try {
            if (rs.next()) {
                rtnUser = new User();
                rtnUser.setId(rs.getInt("id"));
                rtnUser.setName(rs.getString("name"));
                rtnUser.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
            } else {
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rtnUser;
    }
}