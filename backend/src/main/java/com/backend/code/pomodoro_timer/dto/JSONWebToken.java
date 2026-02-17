package com.backend.code.pomodoro_timer.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JSONWebToken {

    private String jwt;
    private boolean isValid;


}
