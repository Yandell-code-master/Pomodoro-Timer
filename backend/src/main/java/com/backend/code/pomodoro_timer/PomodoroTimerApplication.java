package com.backend.code.pomodoro_timer;

import com.fasterxml.uuid.Generators;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class 	PomodoroTimerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PomodoroTimerApplication.class, args);
	}

}
