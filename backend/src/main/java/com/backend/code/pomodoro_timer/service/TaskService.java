package com.backend.code.pomodoro_timer.service;

import com.backend.code.pomodoro_timer.dto.TaskDTO;
import com.backend.code.pomodoro_timer.exception.NotFoundException;
import com.backend.code.pomodoro_timer.mapper.Mapper;
import com.backend.code.pomodoro_timer.model.Task;
import com.backend.code.pomodoro_timer.model.User;
import com.backend.code.pomodoro_timer.repository.TaskRepository;
import com.backend.code.pomodoro_timer.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    // it used to make dependency injection
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // it used to dependency injection
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<TaskDTO> getTasks() {
        return taskRepository.findAll().stream().map(Mapper::toDTO).toList();
    }

    public List<TaskDTO> getTasksById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User doesn't exist");
        }

        return optionalUser.get().getTasks().stream().map(Mapper::toDTO).toList();
    }

    public TaskDTO saveTask(Task task) {
        return Mapper.toDTO(taskRepository.save(task));
    }

    public TaskDTO updateTask(Long id, Task taskWithNewData) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (!optionalTask.isPresent()) {
            throw new NotFoundException("The task doesn't exist");
        }

        Task taskToUpdate = optionalTask.get();

        taskToUpdate.setPomodoros(taskWithNewData.getPomodoros());
        taskToUpdate.setName(taskWithNewData.getName());
        taskToUpdate.setNote(taskWithNewData.getNote());

        taskRepository.save(taskToUpdate);
        return Mapper.toDTO(taskToUpdate);
    }

    public void deleteTask(Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The task doesn't exist"));

        taskRepository.deleteById(id);
    }
}
