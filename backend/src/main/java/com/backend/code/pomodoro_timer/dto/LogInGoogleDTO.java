package com.backend.code.pomodoro_timer.dto;

import com.backend.code.pomodoro_timer.model.UserFromGoogle;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogInGoogleDTO {

    private JSONWebToken jsonWebToken;
    private UserFromGoogle userFromGoogle;
}
