package com.minis.jdbc.core;


import javax.sql.DataSource;
import java.sql.*;

public class JdbcTemplate {

    private DataSource dataSource;
    public JdbcTemplate() {
    }

    public Object query(StatementCallback stmtcallback) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        Object rtnObj = null;

        try {
            con = dataSource.getConnection();
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
            con = dataSource.getConnection();
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

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
