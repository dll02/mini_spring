package com.minis.jdbc.core;


import com.minis.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class JdbcTemplate {

    @Autowired
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
            ArgumentPreparedStatementSetter argumentSetter = new ArgumentPreparedStatementSetter(args);
            argumentSetter.setValues(pstmt);

            rtnObj = pstmtcallback.doInPreparedStatement(pstmt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnObj;
    }


    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) {
        RowMapperResultSetExtractor<T> resultExtractor = new RowMapperResultSetExtractor<>(rowMapper);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            //建立数据库连接
            con = dataSource.getConnection();

            //准备SQL命令语句
            pstmt = con.prepareStatement(sql);
            //设置参数
            ArgumentPreparedStatementSetter argumentSetter = new ArgumentPreparedStatementSetter(args);
            argumentSetter.setValues(pstmt);
            //执行语句
            rs = pstmt.executeQuery();

            //数据库结果集映射为对象列表，返回
            return resultExtractor.extractData(rs);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                pstmt.close();
                con.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
