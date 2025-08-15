package com.helloSpring.identity_service.exception;

public enum ErrorCode {
    ERROR_UNCATEGORIZE( 9999,  "Uncategorize exception" ),
    USER_EXISTED( 1001,  "Use existed" ),
    PASSWORD_ERROR(1002, "Password error"),
    USERNAME_INVALID(1003, "User name invalid"),
    USERNAME_ERROR(1004, "User name is must be 8 charater"),

    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
