package com.backend.code.pomodoro_timer.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TaskDTO {

    private Long id;
    private Integer pomodoros;
    private String name;
    private String note;
}
