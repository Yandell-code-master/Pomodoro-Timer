package com.backend.code.pomodoro_timer.repository;

import com.backend.code.pomodoro_timer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
