package com.helloSpring.identity_service.exception;

public enum ErrorCode {
    ERROR_UNCATEGORIZE( 9999,  "Uncategorize exception" ),
    USER_EXISTED( 1001,  "Use existed" ),
    PASSWORD_ERROR(1002, "Password error"),
    USERNAME_INVALID(1003, "User name invalid"),
    USERNAME_ERROR(1004, "User name is must be 8 charater"),
    USER_NOT_EXISTED(1005, "User not existed"),
    UNAUTHENTICATED(1006, "Unauthenticated"),
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
