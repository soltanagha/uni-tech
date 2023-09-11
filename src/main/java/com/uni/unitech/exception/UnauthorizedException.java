package com.uni.unitech.exception;

import com.uni.unitech.exception.util.ExceptionKeyAndMessage;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends GeneralException {

    private static final long serialVersionUID = 58432132123511L;

    public UnauthorizedException(Enum<? extends ExceptionKeyAndMessage> keyAndMessage, Exception e) {
        super(keyAndMessage, HttpStatus.UNAUTHORIZED);
    }
}
