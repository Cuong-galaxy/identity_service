package com.helloSpring.identity_service.exception;

public class AppException extends  RuntimeException{

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    private ErrorCode errorCode;

    public ErrorCode geterrorCode() {
        return errorCode;
    }

    public void seterrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
