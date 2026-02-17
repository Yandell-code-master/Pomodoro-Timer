package com.backend.code.pomodoro_timer.service;

/*Algo interesante de lo que sucede con los imports cuando utilizas MAVEN es que ya no se hacen como antes cuando se utilizaba un IDE normal como geany
* ahora si ves los imports son así de grandes ya que básicamente es la combinación entre el groupId y el artifactId. Utiliza la carpeta java como si fuera la raíz*/
import com.backend.code.pomodoro_timer.util.CloudinaryConfiguration;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.backend.code.pomodoro_timer.client.GoogleIntegrationService;
import com.backend.code.pomodoro_timer.dto.*;
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

import java.io.IOException;
import java.time.LocalTime;

import com.backend.code.pomodoro_timer.util.JwtGenerator;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.accept.MediaTypeParamApiVersionResolver;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
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
    private final GoogleIntegrationService googleIntegrationService;
    private final ObjectMapper objectMapper;
    private final Cloudinary cloudinary;

    public UserService(UserRepository userRepository, UserFromGoogleRepository userFromGoogleRepository, UserFromEmailRepository userFromEmailRepository, TokenRepository tokenRepository, JwtGenerator jwtGenerator, GoogleIntegrationService googleIntegrationService, ObjectMapper objectMapper, Cloudinary cloudinary) {
        this.userRepository = userRepository;
        this.userFromGoogleRepository = userFromGoogleRepository;
        this.userFromEmailRepository = userFromEmailRepository;
        this.tokenRepository = tokenRepository;
        this.jwtGenerator = jwtGenerator;
        this.googleIntegrationService = googleIntegrationService;
        this.objectMapper = objectMapper;
        this.cloudinary = cloudinary;

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
    public UserFromEmailDTO saveUserFromEmail(UserFromEmail userFromEmail) throws ResendException{
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

        // All the new users have this default name
        // Then the user can change the name easily from the web page
        userFromEmail.setName("newUser");

        // Saving the new user and his new token
        UserFromEmailDTO userFromEmailDTO = Mapper.toDTO(userFromEmailRepository.save(userFromEmail));
        tokenRepository.save(token);

        // Send the email to verify the email and let the user set his new password, can throw exception
        sendEmailToVerify(userFromEmail.getEmail(), tokenToSend);

        return userFromEmailDTO;
    }

    public UserDTO updateUser(Long id, Map<String, Object> dataUpdated) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("The user doesn't exist"));

        // Make the mapp of the new information. All the information is saved in the same User object
        objectMapper.updateValue(userToUpdate, dataUpdated);

        User savedUser = userRepository.save(userToUpdate);

        // Returning DTO depending on the type of User object
        if (savedUser instanceof UserFromEmail) {
            return Mapper.toDTO((UserFromEmail) savedUser);
        } else {
            return Mapper.toDTO(savedUser);
        }
    }

    @Transactional // It is used to make a function a transaction, and the transactions are atomics
    public UserFromEmailDTO updatePassword(String newPassword, String tokenToFind) {

                // Getting the token
                Token token = tokenRepository.findByToken(tokenToFind)
                        .orElseThrow(() -> new TokenExpiredException("The token has expired"));

                // Getting the user
                UserFromEmail ownerOfToken = userFromEmailRepository.findById(token.getUser().getId())
                        .orElseThrow(() -> new TokenExpiredException("The user doesn't exist"));

                // Changing the password and saving to the bd
                // JPA update the bd automatically
                ownerOfToken.setStatus("REGISTERED");
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

        return JSONWebToken.builder()
                .jwt(jwtGenerator.generateToken(userFromEmail.getName()))
                .build();
    }

    public JSONWebToken logInUserGoogle(LogInGoogleDTO logInGoogleDTO) {
        userFromGoogleRepository.findByGoogleId(logInGoogleDTO.getUserFromGoogle().getGoogleId())
                .orElseThrow(() -> new UserNotFound("The user doesn't exist"));

        if (!googleIntegrationService.verifyToken(logInGoogleDTO.getJsonWebToken()).isValid()) {
            throw new JsonWebTokenInvalidException("The token is invalid");
        }

        return logInGoogleDTO.getJsonWebToken();
    }

    public UserFromEmailDTO logInUserEmail(LogInDTO logInDTO) {
        UserFromEmail userFromEmail = userFromEmailRepository.findByEmail(logInDTO.getEmail())
                .orElseThrow(() -> new UserNotFound("The user doesn't exist"));

        if (!BCrypt.checkpw(logInDTO.getPassword(), userFromEmail.getPasswordHash())) {
            throw new PasswordIncorrect("The password inserted is incorrect");
        }

        return Mapper.toDTO(userFromEmail);
    }

    public String uploadPhoto(Long id, MultipartFile photo) {
        try {
            // 1. Subir el archivo usando sus bytes directamente
            // No necesitas guardarlo en tu PC, Cloudinary lee los bytes de la RAM
            Map uploadResult = cloudinary.uploader().upload(photo.getBytes(), ObjectUtils.asMap(
                    "folder", "users",
                    "resource_type", "auto"
            ));

            // 2. Extraer la URL que Cloudinary nos generó al subirlo
            String url = (String) uploadResult.get("secure_url");

            // Update the user with the new URL image
            UserFromEmail userFromEmail = userFromEmailRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("The user doesn't exist"));

            userFromEmail.setPicture(url);
            userRepository.save(userFromEmail);

            return url;
        } catch (IOException e) {
            throw new RuntimeException("Error al leer los bytes del archivo", e);
        }
    }

    public UserFromEmailDTO getUserFromEmail(Long id) {
        return Mapper.toDTO(userFromEmailRepository.findById(id).orElseThrow(() -> new NotFoundException("The user doesn't exist")));
    }

    public void deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("The user doesn't exist"));

        userRepository.deleteById(id);
    }
}
