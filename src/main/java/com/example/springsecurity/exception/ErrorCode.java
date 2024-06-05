package com.example.springsecurity.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    USER_EXISTEED(1001, "User existed"),
    USER_NOT_EXISTED(1002, "User not existed"),
    USERNAME_INVALID(1003, "Username must be at least 3 characters"),
    PASSWORD_INVALID(1004, "Password must be at least 6 characters"),
    KEY_INVALID(1005, "Key invalid"),
    UNAUTHENTICATED(1006, "Unauthenticated"),
    PARSE_ERROR(1007, "Can not parsing this value"),
    TOKEN_INVALID(1008, "Token is not invalid");

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    int code;
    String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
