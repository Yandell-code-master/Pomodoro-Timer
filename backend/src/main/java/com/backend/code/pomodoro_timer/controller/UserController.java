package com.backend.code.pomodoro_timer.controller;

import com.backend.code.pomodoro_timer.dto.PasswordChangeDTO;
import com.backend.code.pomodoro_timer.dto.UserDTO;
import com.backend.code.pomodoro_timer.model.User;
import com.backend.code.pomodoro_timer.model.UserFromEmail;
import com.backend.code.pomodoro_timer.service.UserService;
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
        public ResponseEntity<UserDTO> saveUserFromEmail(@RequestBody UserFromEmail userFromEmail) {
            UserDTO userDTO =  userService.saveUserFromEmail(userFromEmail);

            return ResponseEntity.ok(userDTO);
        }


        @PostMapping("/set-password")
        public ResponseEntity<UserDTO> updatePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
            UserDTO userDTO = userService.updatePassword(passwordChangeDTO.getPassword(), passwordChangeDTO.getToken());

            return ResponseEntity.ok().body(userDTO);
        }

}
