package com.backend.code.pomodoro_timer.controller;

import com.backend.code.pomodoro_timer.dto.TaskDTO;
import com.backend.code.pomodoro_timer.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "http://localhost:8000")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TaskDTO>> getTaskById(Long id) {
        List<TaskDTO> taskList = taskService.getTasksById(id);

        return ResponseEntity.ok().body(taskList);
    }
}
