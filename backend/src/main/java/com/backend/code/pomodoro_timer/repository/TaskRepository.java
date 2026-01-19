package com.backend.code.pomodoro_timer.repository;

import com.backend.code.pomodoro_timer.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
