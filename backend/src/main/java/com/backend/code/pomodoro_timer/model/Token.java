package com.backend.code.pomodoro_timer.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CollectionIdMutability;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "token")
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token")
    private String token;

    @Column(name = "expires_at")
    private LocalTime expires_date;

}
