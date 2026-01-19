package com.backend.code.pomodoro_timer.controller;

import com.backend.code.pomodoro_timer.dto.UserDTO;
import com.backend.code.pomodoro_timer.model.User;
import com.backend.code.pomodoro_timer.service.UserService;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

        private final UserService userService;

        public UserController(UserService userService){
            this.userService = userService;
        }

        @GetMapping
        public ResponseEntity<UserDTO> createUser(User newUser) {
            UserDTO userDTO  = userService.saveUser(newUser);

            return ResponseEntity.ok().body(userDTO);
        }
}
