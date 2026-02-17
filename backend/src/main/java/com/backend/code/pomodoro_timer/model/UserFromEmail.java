package com.backend.code.pomodoro_timer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users_local")
@Entity
public class UserFromEmail extends User{

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "picture", length = 1000)
    private String picture;
}
