package com.daxiasoftware.validatecode;

public interface IValidateCodeService {

    void create(String account, Integer type, String code);
    
    boolean validate(String account, Integer validateCodeType, String userInputValidateCode);
    
    void cleanUp();
    
    String generateRandomNumber(int length);
}
