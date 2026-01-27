package com.backend.code.pomodoro_timer.repository;

import com.backend.code.pomodoro_timer.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
