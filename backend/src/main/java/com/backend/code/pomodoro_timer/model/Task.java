package com.backend.code.pomodoro_timer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task {

        @Id
        @Column(name = "id", nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "pomodoros")
        private Integer pomodoros;

        @Column(name = "name", nullable = false)
        private String name;

        @Column(name = "note")
        private String note;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;
}
