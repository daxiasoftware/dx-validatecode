package com.daxiasoftware.validatecode;

import com.daxiasoftware.validatecode.impl.ValidateCode;

public interface IValidateCodeStore {

    ValidateCode findByAccountAndType(String account, Integer type);

    void updateAsUsed(ValidateCode vc);

    void updateFailedTimes(ValidateCode vc, int failedTimes);

    int countByAccountAndTypeToday(String account, Integer type);

    void create(ValidateCode vc);

    void deleteExpiredBeforeToday();

}
