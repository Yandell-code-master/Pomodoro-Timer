package com.backend.code.pomodoro_timer.service;

import com.backend.code.pomodoro_timer.dto.JSONWebToken;
import com.backend.code.pomodoro_timer.dto.LogInDTO;
import com.backend.code.pomodoro_timer.dto.UserDTO;
import com.backend.code.pomodoro_timer.exception.*;
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

import com.backend.code.pomodoro_timer.util.JwtGenerator;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;
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
    private final JwtGenerator jwtGenerator;

    public UserService(UserRepository userRepository, UserFromGoogleRepository userFromGoogleRepository, UserFromEmailRepository userFromEmailRepository, TokenRepository tokenRepository, JwtGenerator jwtGenerator) {
        this.userRepository = userRepository;
        this.userFromGoogleRepository = userFromGoogleRepository;
        this.userFromEmailRepository = userFromEmailRepository;
        this.tokenRepository = tokenRepository;
        this.jwtGenerator = jwtGenerator;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public UserDTO saveUserFromGoogle(UserFromGoogle userFromGoogle) {
        if (userFromGoogleRepository.findByGoogleId(userFromGoogle.getGoogleId()).isPresent()) {
            throw new UserAlreadyExistException("The user is already registered");
        }

        return Mapper.toDTO(userFromGoogleRepository.save(userFromGoogle));
    }

    @Transactional
    public UserDTO saveUserFromEmail(UserFromEmail userFromEmail) throws ResendException{
        UUID randomToken = UUID.randomUUID();
        String tokenToSend = randomToken.toString();

        if (userFromEmailRepository.findByEmail(userFromEmail.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("The user is already registered");
        }

        // Creating the token for the new user
        Token token = Token.builder()
                .expires_date(LocalTime.now().plusHours(2)) // expires two hours after the creation time
                .user(userFromEmail)
                .token(BCrypt.hashpw(tokenToSend, BCrypt.gensalt())).build();

        // Saving the new user and his new token
        UserDTO userDTO = Mapper.toDTO(userFromEmailRepository.save(userFromEmail));
        tokenRepository.save(token);

        // Send the email to verify the email and let the user set his new password, can throw exception
        sendEmailToVerify(userFromEmail.getEmail(), tokenToSend);

        return userDTO;
    }

    public UserDTO updateUser(Long id, User userUpdated) {
        User userToUpdate = userRepository.findById(id).get();

        userToUpdate.setName(userUpdated.getName());
        userToUpdate.setTasks(userToUpdate.getTasks());

        userRepository.save(userToUpdate);
        return toDTO(userToUpdate);
    }

    @Transactional // It is used to make a function a transaction, and the transactions are atomics
    public UserDTO updatePassword(String newPassword, String tokenToFind) {

                // Getting the token
                Token token = tokenRepository.findByToken(tokenToFind)
                        .orElseThrow(() -> new TokenExpiredException("The token has expired"));

                // Getting the user
                UserFromEmail ownerOfToken = userFromEmailRepository.findById(token.getUser().getId())
                        .orElseThrow(() -> new TokenExpiredException("The user doesn't exist"));

                // Changing the password and saving to the bd
                // JPA update the bd automatically
                ownerOfToken.setPasswordHash(BCrypt.hashpw(newPassword, BCrypt.gensalt()));

                // Deleting the token because is one use token
                tokenRepository.delete(token);
                return Mapper.toDTO(ownerOfToken);

    }


    public void sendEmailToVerify(String email, String token) throws ResendException{
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
        } catch (ResendException ex) {
            throw new ErrorSendingEmail("There was a error sending the email");
        }
    }

    public JSONWebToken LogInUserEmail(LogInDTO logInDTO) {
        UserFromEmail userFromEmail = userFromEmailRepository.findByEmail(logInDTO.getEmail())
                .orElseThrow(() -> new UserNotFound("The user doesn't exist"));

        if (!BCrypt.checkpw(logInDTO.getPassword(), userFromEmail.getPasswordHash())) {
            throw new PasswordIncorrect("The password passed was incorrect");
        }

        return new JSONWebToken(jwtGenerator.generateToken(userFromEmail.getName()));
    }
}
