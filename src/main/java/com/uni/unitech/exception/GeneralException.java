package com.uni.unitech.exception;

import com.uni.unitech.exception.util.ExceptionKeyAndMessage;
import org.springframework.http.HttpStatus;

public class GeneralException extends RuntimeException {

    private static final long serialVersionUID = 853354321283511L;

    private final String exceptionKey;
    private final HttpStatus httpStatus;

    public GeneralException(Enum<? extends ExceptionKeyAndMessage> keyAndMessage,
                            HttpStatus httpStatus) {
        super(((ExceptionKeyAndMessage) keyAndMessage).getExceptionMessage());
        ExceptionKeyAndMessage key = (ExceptionKeyAndMessage) keyAndMessage;
        this.exceptionKey = key.getExceptionKey();
        this.httpStatus = httpStatus;
    }

    public GeneralException(String message,
                            Enum<? extends ExceptionKeyAndMessage> keyAndMessage,
                            HttpStatus httpStatus) {
        super(message);
        ExceptionKeyAndMessage key = (ExceptionKeyAndMessage) keyAndMessage;
        this.exceptionKey = key.getExceptionKey();
        this.httpStatus = httpStatus;
    }

    public GeneralException(String message,
                            String key,
                            HttpStatus httpStatus) {
        super(message);
        this.exceptionKey = key;
        this.httpStatus = httpStatus;
    }

    public GeneralException(Throwable cause,
                            Enum<? extends ExceptionKeyAndMessage> keyAndMessage,
                            HttpStatus httpStatus) {
        super(((ExceptionKeyAndMessage) keyAndMessage).getExceptionMessage(), cause);
        ExceptionKeyAndMessage key = (ExceptionKeyAndMessage) keyAndMessage;
        this.exceptionKey = key.getExceptionKey();
        this.httpStatus = httpStatus;
    }

    public String getExceptionKey() {
        return exceptionKey;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
