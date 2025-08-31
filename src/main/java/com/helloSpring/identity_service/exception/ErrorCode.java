package com.helloSpring.identity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    //
    ERROR_UNCATEGORIZE( 9999,  "Uncategorize exception", HttpStatus.INTERNAL_SERVER_ERROR ),
    INVALID_KEY(1111, "Invalid key", HttpStatus.BAD_REQUEST),
    USER_EXISTED( 1001,  "Use existed", HttpStatus.BAD_REQUEST ),
    PASSWORD_ERROR(1002, "Password is must be  {min} charater", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "User name invalid", HttpStatus.BAD_REQUEST),
    USERNAME_ERROR(1004, "User name is must be  {min} charater", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),

    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }


    private int code;
    private String message;
    private HttpStatusCode statusCode;


}
