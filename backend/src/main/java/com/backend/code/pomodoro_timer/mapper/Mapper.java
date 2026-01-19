package com.backend.code.pomodoro_timer.mapper;

import com.backend.code.pomodoro_timer.dto.TaskDTO;
import com.backend.code.pomodoro_timer.dto.UserDTO;
import com.backend.code.pomodoro_timer.model.Task;
import com.backend.code.pomodoro_timer.model.User;

// We create this class to be able to mapp the classes easily
public class Mapper {

    public static UserDTO toDTO(User user) {
        if (user == null) return new UserDTO("Defualt");

        return UserDTO.builder()
                .name(user.getName())
                .build();
    }

    public static TaskDTO toDTO(Task task) {
        if (task == null) return new TaskDTO(0, "Default", "Nothing");

        return TaskDTO.builder()
                .pomodoros(task.getPomodoros())
                .name(task.getName())
                .note(task.getNote())
                .build();
    }
}
