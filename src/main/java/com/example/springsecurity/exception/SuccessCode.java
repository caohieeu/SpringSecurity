package com.example.springsecurity.exception;

public enum SuccessCode {
    UPDATE_SUCCESS(1000, "Update successfully"),
    DELETE_SUCCESS(1000, "Delete successfully");
    int code;
    String message;

    SuccessCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

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
