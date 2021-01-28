package com.daxiasoftware.validatecode.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.sql.DataSource;

import com.daxiasoftware.validatecode.IValidateCodeStore;

public class ValidateCodeJdbcStore implements IValidateCodeStore {
    private static String TABLE = "dx_validate_code";
    private static String COLUMNS = "id, account, code, type, create_time, expire_time, failed_times, is_used";
    private DataSource dataSource;
    
    public ValidateCodeJdbcStore(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public ValidateCode findByAccountAndType(String account, Integer type) {
        try {
            String sql = "select " + COLUMNS + " from " + TABLE + " where account = ? and type = ? and is_used = 0 order by create_time desc limit 1";
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, account);
            statement.setInt(2, type);
            ResultSet rs = statement.executeQuery();
            ValidateCode vc = null;
            if (rs.next()) {
                vc = new ValidateCode();
                vc.setId(rs.getString("id"));
                vc.setAccount(rs.getString("account"));
                vc.setCode(rs.getString("code"));
                vc.setType(rs.getInt("type"));
                vc.setCreateTime(rs.getTimestamp("create_time"));
                vc.setExpireTime(rs.getTimestamp("expire_time"));
                vc.setFailedTimes(rs.getInt("failed_times"));
                vc.setUsed(rs.getBoolean("is_used"));
            }
            
            rs.close();
            statement.close();
            conn.close();
            return vc;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void updateAsUsed(ValidateCode vc) {
        try {
            String sql = "update " + TABLE + " set is_used = ? where id = ?";
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setBoolean(1, true);
            statement.setString(2, vc.getId());
            statement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void updateFailedTimes(ValidateCode vc, int failedTimes) {
        try {
            String sql = "update " + TABLE + " set failed_times = ? where id = ?";
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, failedTimes);
            statement.setString(2, vc.getId());
            statement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public int countByAccountAndTypeToday(String account, Integer type) {
        try {
            String sql = "select count(*) as cnt from " + TABLE + " where account = ? and type = ? and create_time > ? ";
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, account);
            statement.setInt(2, type);
            statement.setDate(3, new Date(getBeginningOfADay(new java.util.Date()).getTime()));
            ResultSet rs = statement.executeQuery();
            int cnt = 0;
            if (rs.next()) {
                cnt = rs.getInt("cnt");
            }
            
            rs.close();
            statement.close();
            conn.close();
            return cnt;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public java.util.Date getBeginningOfADay(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    public void create(ValidateCode vc) {
        try {
            String sql = "insert into " + TABLE + "(id, account, code, type, create_time, expire_time, failed_times, is_used) ";
            sql += "values(?, ?, ?, ?, ?, ?, ?, ?)";
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, vc.getId());
            statement.setString(2, vc.getAccount());
            statement.setString(3, vc.getCode());
            statement.setInt(4, vc.getType());
            statement.setTimestamp(5, new Timestamp(vc.getCreateTime().getTime()));
            statement.setTimestamp(6, new Timestamp(vc.getExpireTime().getTime()));
            statement.setInt(7, vc.getFailedTimes());
            statement.setBoolean(8, vc.isUsed());
            
            statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

//    private Date toSqlDate(java.util.Date date) {
//        return new Date(date)
//    }
    
    public void deleteExpiredBeforeToday() {
        try {
            String sql = "delete from " + TABLE + " where expire_time < ?";
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setDate(1, new Date(getBeginningOfADay(new java.util.Date()).getTime()));
            
            statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }   
    }

}
