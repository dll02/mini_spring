package com.minis.jdbc.core;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class JdbcTemplate {
    public JdbcTemplate() {
    }
    public Object query(String sql) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Object rtnObj = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo?characterEncoding=UTF8", "root", "12345678");
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            //调用返回数据处理方法，由程序员自行实现
            rtnObj = doInStatement(rs);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                rs.close();
                stmt.close();
                con.close();
            } catch (Exception e) {
            }
        }
        return rtnObj;
    }

    protected abstract  Object doInStatement(ResultSet rs);
}
