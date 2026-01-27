package com.backend.code.pomodoro_timer.dto;

import com.backend.code.pomodoro_timer.model.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeDTO {
    private String password;
    private String token;
}
