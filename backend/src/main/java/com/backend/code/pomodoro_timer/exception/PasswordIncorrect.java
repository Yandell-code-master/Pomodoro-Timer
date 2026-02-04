package com.backend.code.pomodoro_timer.exception;

public class PasswordIncorrect extends RuntimeException {
    public PasswordIncorrect(String message) {
        super(message);
    }
}
