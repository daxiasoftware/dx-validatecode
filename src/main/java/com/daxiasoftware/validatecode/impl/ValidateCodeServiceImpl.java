package com.daxiasoftware.validatecode.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.lang.time.DateUtils;

import com.daxiasoftware.validatecode.IValidateCodeService;
import com.daxiasoftware.validatecode.IValidateCodeStore;

public class ValidateCodeServiceImpl implements IValidateCodeService {
    // 每天每种类型默认发送多少次
    private int maxSendTimesPerTypeInADay = 100;
    private int expireMinutes = 5;
    private int maxFailTimes = 0;
    private IValidateCodeStore store; 
    public ValidateCodeServiceImpl(DataSource dataSource) {
        this.store = new ValidateCodeJdbcStore(dataSource);
        Properties properties = new Properties();
        try {
            properties.load(ValidateCodeServiceImpl.class.getClassLoader().getResourceAsStream("validateCode.properties"));
            maxFailTimes = Integer.parseInt(properties.getProperty("maxFailTimes"));
            expireMinutes = Integer.parseInt(properties.getProperty("expireMinutes"));
            maxSendTimesPerTypeInADay = Integer.parseInt(properties.getProperty("maxSendTimesPerTypeInADay"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void create(String account, Integer type, String code) {
        ValidateCode vc = new ValidateCode();
        int timesToday = store.countByAccountAndTypeToday(account, type);
        if (timesToday > maxSendTimesPerTypeInADay) {
            throw new RuntimeException("今日发送次数超出限制");
        }
        vc.setId(UUID.randomUUID().toString());
        vc.setAccount(account);
        vc.setCode(code);
        vc.setCreateTime(new Date());
        vc.setExpireTime(DateUtils.addMinutes(vc.getCreateTime(), expireMinutes));
        vc.setFailedTimes(0);
        vc.setType(type);
        vc.setUsed(false);
     
        store.create(vc);
    }
    
    public boolean validate(String account, Integer validateCodeType, String userInputValidateCode) {
        ValidateCode vc = store.findByAccountAndType(account, validateCodeType);
        if (vc == null) {
            return false;
        }
        
        Date now = new Date();
        if (now.after(vc.getExpireTime())) {
            return false;
        }
        if (vc.getFailedTimes() >= maxFailTimes) {
            return false;
        }
        
        if (vc.getCode().equalsIgnoreCase(userInputValidateCode)) {
            store.updateAsUsed(vc);
            return true;
        } else {
            store.updateFailedTimes(vc, vc.getFailedTimes() + 1);
            return false;
        }
    }

    public void cleanUp() {
        store.deleteExpiredBeforeToday();
    }

    public String generateRandomNumber(int length) {
        if (length < 1 || length > 10) {
            throw new RuntimeException("Invalid length, shoud >= 1 && <= 10");
        }
        String str = "";
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            str += rnd.nextInt(10);
        }
        return str;
    }

    
}
