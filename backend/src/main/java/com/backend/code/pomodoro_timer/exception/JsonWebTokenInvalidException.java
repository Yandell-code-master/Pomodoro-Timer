package com.backend.code.pomodoro_timer.exception;

public class JsonWebTokenInvalidException extends RuntimeException {
    public JsonWebTokenInvalidException(String message) {
        super(message);
    }
}
