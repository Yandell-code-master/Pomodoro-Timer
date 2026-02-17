package com.backend.code.pomodoro_timer.controller;

import com.backend.code.pomodoro_timer.dto.*;
import com.backend.code.pomodoro_timer.model.User;
import com.backend.code.pomodoro_timer.model.UserFromEmail;
import com.backend.code.pomodoro_timer.model.UserFromGoogle;
import com.backend.code.pomodoro_timer.service.UserService;
import com.resend.core.exception.ResendException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:8000")
public class UserController {

        private final UserService userService;

        public UserController(UserService userService){
            this.userService = userService;
        }

        @PostMapping("/save-user-email")
        public ResponseEntity<UserFromEmailDTO> saveUserFromEmail(@RequestBody UserFromEmail userFromEmail) throws ResendException {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userService.saveUserFromEmail(userFromEmail));
        }

        @PostMapping("/save-user-google")
        public ResponseEntity<?> saveUserFromGoogle(@RequestBody UserFromGoogle userFromGoogle) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userService.saveUserFromGoogle(userFromGoogle));
        }


        @PostMapping("/set-password")
        public ResponseEntity<UserFromEmailDTO> updatePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
            UserFromEmailDTO userFromEmailDTO = userService.updatePassword(passwordChangeDTO.getPassword(), passwordChangeDTO.getToken());

            return ResponseEntity.ok().body(userFromEmailDTO);
        }

        @PostMapping("/log-in-google")
        public ResponseEntity<JSONWebToken> logInUserGoogle(@RequestBody LogInGoogleDTO logInGoogleDTO) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userService.logInUserGoogle(logInGoogleDTO));
        }

        @PostMapping("/log-in-email")
        public ResponseEntity<UserFromEmailDTO> logInUserGoogle(@RequestBody LogInDTO logInDTO) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userService.logInUserEmail(logInDTO));
        }

        /*Cuando estoy haciendo API's restfull (REpresentational State Transfer) las URL deben de apuntar a un recurso dentro de mi base de datos, no deben de ser acciones,
        * ya que las acciones las estamos poniendo dentro del verbo en este caso PATCH. Tambien hay que tener en cuenta que en vez de utilizar un URL por cada acción que quieres
        * lo mejor es que utilices solo un URL por ejemplo un PATCH y en vez de crear una función para cada atributo del objeto tu haces una validación interna para saber
        * qué atributo debes actualizar*/
        @PatchMapping("/{id}")
        public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                                  @RequestBody Map<String, Object> dataUptaded
        ) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userService.updateUser(id, dataUptaded));
        }

        // Puede ser extraño que utilicemos RequestParam cuando lo estamos mandando por el body desde el front pero lo que pasa que esta es la forma de obtener los datos que trae
        // una peticion con content type multipart
        @PostMapping("/{userId}/photo")
        public ResponseEntity<String> uploadPhoto(@PathVariable Long userId,
                                                  @RequestParam("userPhoto") MultipartFile photo)
        {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userService.uploadPhoto(userId, photo));
        }

        @GetMapping("/email-type/{id}")
        public ResponseEntity<UserFromEmailDTO> getUser(@PathVariable Long id)
        {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userService.getUserFromEmail(id));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
            userService.deleteUser(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }


}
