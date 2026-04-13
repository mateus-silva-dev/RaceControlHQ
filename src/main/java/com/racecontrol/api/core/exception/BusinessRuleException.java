package com.racecontrol.api.core.exception;

import com.racecontrol.api.core.code.Code;
import lombok.Getter;

@Getter
public class BusinessRuleException extends RuntimeException {

    private final Code code;

    public BusinessRuleException(String message, Code code) {
        super(message);
        this.code = code;
    }

    public BusinessRuleException(String message) {
       this(message, Code.BUSINESS_RULE);
    }

}
