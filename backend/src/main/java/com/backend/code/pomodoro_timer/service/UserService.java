package com.backend.code.pomodoro_timer.service;

import com.backend.code.pomodoro_timer.dto.UserDTO;
import com.backend.code.pomodoro_timer.exception.ErrorSendingEmail;
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

        // Creating the token for the new user
        Token token = Token.builder()
                .expires_date(LocalTime.now().plusHours(2)) // expires two hours after the creation time
                .user(userFromEmail)
                .token(BCrypt.hashpw(tokenToSend, BCrypt.gensalt())).build();

        // Send the email to verify the email and let the user set his new password
        if (sendEmailToVerify(userFromEmail.getEmail(), tokenToSend)) {
            // Saving the new user and his new token
            UserDTO userDTO = Mapper.toDTO(userFromEmailRepository.save(userFromEmail));
            tokenRepository.save(token);

            return userDTO;
        } else {
            throw new ErrorSendingEmail("There was an error sending the email");
        }
    }

    public UserDTO updateUser(Long id, User userUpdated) {
        User userToUpdate = userRepository.findById(id).get();

        userToUpdate.setName(userUpdated.getName());
        userToUpdate.setTasks(userToUpdate.getTasks());

        userRepository.save(userToUpdate);
        return toDTO(userToUpdate);
    }

    public UserDTO updatePassword(String newPassword, String tokenToFind) {
        List<User> users = getUsers();

        for(User user : users) {
            List<Token> tokens = user.getResetPasswordTokens();

            for (Token token : tokens) {
                if(BCrypt.checkpw(tokenToFind, token.getToken())) {
                    if (user instanceof UserFromEmail) {
                        UserFromEmail userFromEmail = (UserFromEmail) user;

                        userFromEmail.setPasswordHash(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
                        userFromEmail = userFromEmailRepository.save(userFromEmail);
                        tokenRepository.delete(token);
                        return Mapper.toDTO(userFromEmail);
                    }
                }
            }
        }

        throw new TokenExpiredException("The token have expired");
    }


    public boolean sendEmailToVerify(String email, String token) {
        // getting the environment variable from .env file
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("RESEND_API_KEY");

        Resend resend = new Resend(apiKey);

        String URLSetPassword = "http://localhost:8000/set-password.html?token=" + token;

        CreateEmailOptions emailConfiguration = CreateEmailOptions.builder()
                .from("Support <onboarding@resend.dev>")
                .to(email)
                .subject("Reset your password")
                .html("<strong>Hi!</strong><br>" +
                        "Click in the link below to change your password:<br>" +
                        "<a href='" + URLSetPassword + "'>Reset password</a>")
                .build();


        try {
            CreateEmailResponse data = resend.emails().send(emailConfiguration);
            return true;
        } catch (ResendException e) {
            e.printStackTrace();
            return false;
        }
    }
}
