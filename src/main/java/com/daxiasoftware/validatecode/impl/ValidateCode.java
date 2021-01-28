package com.daxiasoftware.validatecode.impl;

import java.util.Date;

public class ValidateCode {

    private String id;
    private String account;
    private String code;
    private int type;
    private Date createTime;
    private Date expireTime;
    private int failedTimes;
    private boolean used;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public int getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getExpireTime() {
        return expireTime;
    }
    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
    public int getFailedTimes() {
        return failedTimes;
    }
    public void setFailedTimes(Integer failedTimes) {
        this.failedTimes = failedTimes;
    }
    public boolean isUsed() {
        return used;
    }
    public void setUsed(boolean used) {
        this.used = used;
    }
   
}
