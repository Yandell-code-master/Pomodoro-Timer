package com.backend.code.pomodoro_timer.exception;

public class ErrorSendingEmail extends RuntimeException {
    public ErrorSendingEmail(String message) {
        super(message);
    }
}
