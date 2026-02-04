package com.backend.code.pomodoro_timer.controller;

import com.backend.code.pomodoro_timer.dto.JSONWebToken;
import com.backend.code.pomodoro_timer.dto.LogInDTO;
import com.backend.code.pomodoro_timer.dto.PasswordChangeDTO;
import com.backend.code.pomodoro_timer.dto.UserDTO;
import com.backend.code.pomodoro_timer.exception.UserAlreadyExistException;
import com.backend.code.pomodoro_timer.model.User;
import com.backend.code.pomodoro_timer.model.UserFromEmail;
import com.backend.code.pomodoro_timer.model.UserFromGoogle;
import com.backend.code.pomodoro_timer.service.UserService;
import com.resend.core.exception.ResendException;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:8000")
public class UserController {

        private final UserService userService;

        public UserController(UserService userService){
            this.userService = userService;
        }

        @PostMapping("/save-user-email")
        public ResponseEntity<UserDTO> saveUserFromEmail(@RequestBody UserFromEmail userFromEmail) throws ResendException {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userService.saveUserFromEmail(userFromEmail));
        }

        @PostMapping("/save-user-google")
        public ResponseEntity<?> saveUserFromGoogle(@RequestBody UserFromGoogle userFromGoogle) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userService.saveUserFromGoogle(userFromGoogle));
        }


        @PostMapping("/set-password")
        public ResponseEntity<UserDTO> updatePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
            UserDTO userDTO = userService.updatePassword(passwordChangeDTO.getPassword(), passwordChangeDTO.getToken());

            return ResponseEntity.ok().body(userDTO);
        }

        @PostMapping("/log-in-email")
        public ResponseEntity<JSONWebToken> logInUserEmail(LogInDTO logInDTO) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userService.LogInUserEmail(logInDTO));
        }
}
