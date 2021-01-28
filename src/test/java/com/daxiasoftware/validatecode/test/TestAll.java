package com.daxiasoftware.validatecode.test;

import javax.print.attribute.standard.Severity;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

import com.daxiasoftware.validatecode.IValidateCodeService;
import com.daxiasoftware.validatecode.impl.ValidateCodeServiceImpl;

public class TestAll {

    @Test
    public void testAll() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("sa");
        IValidateCodeService service = new ValidateCodeServiceImpl(dataSource);
        
        service.create("15074921644", 1, "2345");
        boolean isValid = service.validate("15074921644", 1, "23245");
        System.out.println(isValid);
    }
}
