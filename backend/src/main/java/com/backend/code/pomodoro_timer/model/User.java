package com.backend.code.pomodoro_timer.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tasks")
    @OneToMany(mappedBy = "user")
    private ArrayList<Task> tasks;

    @Column(name = "name")
    private String name;
}
