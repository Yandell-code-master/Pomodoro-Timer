package com.backend.code.pomodoro_timer.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserFromEmailDTO extends UserDTO{

    private String email;
    private String picture;
    private String name;
}
