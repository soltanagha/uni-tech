package com.uni.unitech.exception;

import com.uni.unitech.exception.util.ExceptionKeyAndMessage;
import org.springframework.http.HttpStatus;

public class NotFoundException extends GeneralException {

    private static final long serialVersionUID = 58432132465811L;

    public NotFoundException(Enum<? extends ExceptionKeyAndMessage> keyAndMessage) {
        super(keyAndMessage, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(String message,
                             Enum<? extends ExceptionKeyAndMessage> key) {
        super(message, key, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(String message,
                             String key) {
        super(message, key, HttpStatus.BAD_REQUEST);
    }
}
