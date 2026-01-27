package com.backend.code.pomodoro_timer.repository;

import com.backend.code.pomodoro_timer.model.UserFromGoogle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFromGoogleRepository extends JpaRepository<UserFromGoogle, Long> {
}
