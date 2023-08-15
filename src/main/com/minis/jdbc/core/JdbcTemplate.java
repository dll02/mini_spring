package com.minis.jdbc.core;


import java.sql.*;

public class JdbcTemplate {
    public JdbcTemplate() {
    }

    public Object query(StatementCallback stmtcallback) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        Object rtnObj = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo?characterEncoding=UTF8", "root", "12345678");
            stmt = con.createStatement();
            return stmtcallback.doInStatement(stmt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                stmt.close();
                con.close();
            } catch (Exception e) {
            }
        }
        return rtnObj;
    }

    public Object query(String sql, Object[] args, PreparedStatementCallback pstmtcallback) {
        PreparedStatement pstmt = null;
        Connection con = null;
        Object rtnObj = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo?characterEncoding=UTF8", "root", "12345678");
            pstmt = con.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) { //设置参数
                Object arg = args[i];
                //按照不同的数据类型调用JDBC的不同设置方法
                if (arg instanceof String) {
                    pstmt.setString(i + 1, (String) arg);
                } else if (arg instanceof Integer) {
                    pstmt.setInt(i + 1, (int) arg);
                }
            }

            rtnObj= pstmtcallback.doInPreparedStatement(pstmt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnObj;
    }
}
