package com.backend.code.pomodoro_timer.repository;

import com.backend.code.pomodoro_timer.model.UserFromEmail;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFromEmailRepository extends JpaRepository<UserFromEmail, Long> {

    public Optional<UserFromEmail> findByEmail(String email);
}
