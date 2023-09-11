package com.uni.unitech.exception.util;

public enum ExceptionCodes implements ExceptionKeyAndMessage {

    DUPLICATE_DATA_EXCEPTION("3816", "This pin already exits."),
    INVALID_PIN_OR_PASSWORD("3836","User pin or password wrong."),
    USER_NOT_FOUND("3837", "User not found"), 
    JWT_EXPIRED("3840", "JWT is expired"),
    INVALID_TOKEN("3844","Invalid token"),
    METHOD_ARGUMENT_TYPE_MISMATCH("3653"),
    METHOD_ARGUMENT_NOT_VALID("3700"),
    INVALID_PIN_OR_SYMBOL("4034","Invalid pin or symbol"),
    INVALID_ACCOUNT_NUMBER("4035","Invalid account number"),
    INSUFFICIENT_BALANCE("3843","Insufficient balance"),
    TRANSFER_BETWEEN_SAME_ACCOUNT("3845","Attempt transfer in same account"),
    INACTIVE_ACCOUNT("4036","Account is inactive"),
    CURRENCY_RATE_NOT_FOUND("4037","Currency rate doesn't exist in database");

    private String exceptionKey;
    private final String message;

    ExceptionCodes(String exceptionKey, String message) {
        this.exceptionKey = exceptionKey;
        this.message = message;
    }

    ExceptionCodes(String exceptionKey) {
        this.exceptionKey = exceptionKey;
        this.message = "NO Message";
    }

    @Override
    public String getExceptionKey() {
        return exceptionKey;
    }

    @Override
    public String getExceptionMessage() {
        return message;
    }

    public ExceptionCodes exceptionKey(String exceptionKey) {
        this.exceptionKey = exceptionKey;
        return this;
    }
}
