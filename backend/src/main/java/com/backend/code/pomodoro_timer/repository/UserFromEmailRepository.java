package com.backend.code.pomodoro_timer.repository;

import com.backend.code.pomodoro_timer.model.UserFromEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFromEmailRepository extends JpaRepository<UserFromEmail, Long> {
}
