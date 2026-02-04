package com.backend.code.pomodoro_timer.repository;

import com.backend.code.pomodoro_timer.model.UserFromGoogle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFromGoogleRepository extends JpaRepository<UserFromGoogle, Long> {

    public Optional<UserFromGoogle> findByGoogleId(String googleId);
}
