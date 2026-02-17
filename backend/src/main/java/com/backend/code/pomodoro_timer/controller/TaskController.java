package com.backend.code.pomodoro_timer.controller;

import com.backend.code.pomodoro_timer.dto.TaskDTO;
import com.backend.code.pomodoro_timer.model.Task;
import com.backend.code.pomodoro_timer.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<TaskDTO>> getTaskById(@PathVariable Long id) {
        List<TaskDTO> taskList = taskService.getTasksById(id);

        return ResponseEntity.ok().body(taskList);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody Task task) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.saveTask(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        System.out.println("Hola");
        taskService.deleteTask(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
