package com.backend.code.pomodoro_timer.mapper;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.backend.code.pomodoro_timer.dto.TaskDTO;
import com.backend.code.pomodoro_timer.dto.UserDTO;
import com.backend.code.pomodoro_timer.model.Task;
import com.backend.code.pomodoro_timer.model.User;
import com.backend.code.pomodoro_timer.model.UserFromEmail;
import com.backend.code.pomodoro_timer.dto.UserFromEmailDTO;

// We create this class to be able to mapp the classes easily
public class Mapper {

    public static UserDTO toDTO(User user) {
        if (user == null) throw new NullPointerException("The user that you passed to the Mapper is null");

        return UserDTO.builder()
                .name(user.getName())
                .build();
    }

    public static UserFromEmailDTO toDTO(UserFromEmail userFromEmail) {
        if (userFromEmail == null) return UserFromEmailDTO.builder().name("Default").build();

        return UserFromEmailDTO.builder()
                .name(userFromEmail.getName())
                .picture(userFromEmail.getPicture())
                .email(userFromEmail.getEmail())
                .id(userFromEmail.getId())
                .build();
    }

    public static TaskDTO toDTO(Task task) {
        if (task == null) throw new NullPointerException("The task that you passed to the Mapper is null");

        return TaskDTO.builder()
                .pomodoros(task.getPomodoros())
                .name(task.getName())
                .note(task.getNote())
                .id(task.getId())
                .build();
    }
}
