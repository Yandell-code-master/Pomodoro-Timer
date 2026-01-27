package com.backend.code.pomodoro_timer.service;

import com.backend.code.pomodoro_timer.dto.UserDTO;
import com.backend.code.pomodoro_timer.exception.TokenExpiredException;
import com.backend.code.pomodoro_timer.mapper.Mapper;
import com.backend.code.pomodoro_timer.model.Token;
import com.backend.code.pomodoro_timer.model.User;
import com.backend.code.pomodoro_timer.model.UserFromEmail;
import com.backend.code.pomodoro_timer.model.UserFromGoogle;
import com.backend.code.pomodoro_timer.repository.TokenRepository;
import com.backend.code.pomodoro_timer.repository.UserFromEmailRepository;
import com.backend.code.pomodoro_timer.repository.UserFromGoogleRepository;
import com.backend.code.pomodoro_timer.repository.UserRepository;
import java.time.LocalTime;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.backend.code.pomodoro_timer.mapper.Mapper.toDTO;

@Service
public class UserService {

    // Used to make dependency injection
    private final UserRepository userRepository;
    private final UserFromGoogleRepository userFromGoogleRepository;
    private final UserFromEmailRepository userFromEmailRepository;
    private final TokenRepository tokenRepository;

    public UserService(UserRepository userRepository, UserFromGoogleRepository userFromGoogleRepository, UserFromEmailRepository userFromEmailRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.userFromGoogleRepository = userFromGoogleRepository;
        this.userFromEmailRepository = userFromEmailRepository;
        this.tokenRepository = tokenRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public UserDTO saveUserFromGoogle(UserFromGoogle userFromGoogle) {
        return Mapper.toDTO(userFromGoogleRepository.save(userFromGoogle));
    }

    public UserDTO saveUserFromEmail(UserFromEmail userFromEmail) {
        UUID randomToken = UUID.randomUUID();
        String tokenToSend = randomToken.toString();

        Token token = Token.builder()
                .expires_date(LocalTime.now().plusHours(2)) // expires two hours after the creation time
                .user(userFromEmail)
                .token(BCrypt.hashpw(tokenToSend, BCrypt.gensalt())).build();

        // saving the token in the bd
        tokenRepository.save(token);

        // I think this line have to go
        userFromEmail.setPasswordHash(BCrypt.hashpw(userFromEmail.getPasswordHash(), BCrypt.gensalt()));


        sendEmailToVerify(userFromEmail.getEmail(), tokenToSend);

        return Mapper.toDTO(userFromEmailRepository.save(userFromEmail));
    }

    public UserDTO updateUser(Long id, User userUpdated) {
        User userToUpdate = userRepository.findById(id).get();

        userToUpdate.setName(userUpdated.getName());
        userToUpdate.setTasks(userToUpdate.getTasks());

        userRepository.save(userToUpdate);
        return toDTO(userToUpdate);
    }

    public UserDTO updatePassword(String newPassword, String token) {
        List<User> users = getUsers();

        for(User user : users) {
            if(user.getResetPasswordToken().getToken().equals(token)) {
                if (user instanceof UserFromEmail) {
                    UserFromEmail userFromEmail = (UserFromEmail) user;

                    userFromEmail.setPasswordHash(newPassword);
                    tokenRepository.delete(userFromEmail.getResetPasswordToken());

                    return Mapper.toDTO(userFromEmailRepository.save(userFromEmail));
                }
            }
        }

        throw new TokenExpiredException("The token have expired");
    }


    public void sendEmailToVerify(String email, String token) {
        // getting the environment variable from .env file
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("RESEND_API_KEY");

        Resend resend = new Resend(apiKey);

        String URLSetPassword = "http://localhost:8080/set-password.html?token=" + token;

        CreateEmailOptions emailConfiguration = CreateEmailOptions.builder()
                .from("Support just making testing")
                .to(email)
                .subject("Reset your password")
                .html("<strong>Hi!</strong><br>" +
                        "Click in the link below to change your password:<br>" +
                        "<a href='" + URLSetPassword + "'>Reset password</a>")
                .build();


        try {
            CreateEmailResponse data = resend.emails().send(emailConfiguration);
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }
}
