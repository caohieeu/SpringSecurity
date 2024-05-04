package com.example.springsecurity.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    USER_EXISTEED(1001, "User xisted"),
    USER_NOT_EXISTED(1002, "User not existed"),
    USERNAME_INVALID(1003, "Username must be at least 3 characters"),
    PASSWORD_INVALID(1004, "Password must be at least 6 characters"),
    KEY_INVALID(1005, "Key invalid");

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
