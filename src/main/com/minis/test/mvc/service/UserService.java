package com.minis.test.mvc.service;

import com.minis.batis.SqlSession;
import com.minis.batis.SqlSessionFactory;
import com.minis.beans.factory.annotation.Autowired;
import com.minis.jdbc.core.JdbcTemplate;
import com.minis.jdbc.core.RowMapper;
import com.minis.test.mvc.dao.UserJdbcImpl;
import com.minis.test.mvc.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    public User getUserInfoBySession(int userid) {
        //final String sql = "select id, name,birthday from user where id=" + userid;
        String sqlid = "com.minis.test.mvc.entity.User.getUserInfo";
        SqlSession   sqlSession = sqlSessionFactory.openSession();
        return (User) sqlSession.selectOne(sqlid,new Object[]{userid}, (pstmt) -> {
            ResultSet rs = pstmt.executeQuery();
            User rtnUser = null;
            if (rs.next()) {
                rtnUser = new User();
                rtnUser.setId(userid);
                rtnUser.setName(rs.getString("name"));
                rtnUser.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
            }
            return rtnUser;
        });
    }

    public User getUserInfo(int userid) {
        final String sql = "select id, name,birthday from user where id=" + userid;
        return (User) jdbcTemplate.query((stmt) -> {
            ResultSet rs = stmt.executeQuery(sql);
            User rtnUser = null;
            if (rs.next()) {
                rtnUser = new User();
                rtnUser.setId(userid);
                rtnUser.setName(rs.getString("name"));
                rtnUser.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
            }
            return rtnUser;
        });
    }

    public User getUserInfoPre(int userid) {
        final String sql = "select id, name,birthday from user where id=?";
        return (User) jdbcTemplate.query(sql, new Object[]{userid}, (pstmt) -> {
            ResultSet rs = pstmt.executeQuery();
            User rtnUser = null;
            if (rs.next()) {
                rtnUser = new User();
                rtnUser.setId(userid);
                rtnUser.setName(rs.getString("name"));
            }
            return rtnUser;
        });
    }
    public List<User> getUsersByExtractor(int userid) {
        final String sql = "select id, name,birthday from user where id>?";
        return (List<User>)jdbcTemplate.query(sql, new Object[]{userid},
                // 设置每一行结果数据的映射器
                new RowMapper<User>(){
                    public User mapRow(ResultSet rs, int i) throws SQLException {
                        User rtnUser = new User();
                        rtnUser.setId(rs.getInt("id"));
                        rtnUser.setName(rs.getString("name"));
                        rtnUser.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));

                        return rtnUser;
                    }
                }
        );
    }

}
