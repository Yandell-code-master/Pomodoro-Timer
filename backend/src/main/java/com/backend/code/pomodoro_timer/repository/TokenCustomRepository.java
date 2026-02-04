package com.backend.code.pomodoro_timer.repository;

import com.backend.code.pomodoro_timer.model.Token;

import java.util.Optional;

public interface TokenCustomRepository {

    public Optional<Token> findByToken(String tokenToFind);
}
