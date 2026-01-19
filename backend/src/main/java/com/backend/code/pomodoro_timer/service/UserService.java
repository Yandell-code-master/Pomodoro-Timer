package com.backend.code.pomodoro_timer.service;

import com.backend.code.pomodoro_timer.dto.UserDTO;
import com.backend.code.pomodoro_timer.mapper.Mapper;
import com.backend.code.pomodoro_timer.model.User;
import com.backend.code.pomodoro_timer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    // Used to make dependency injection
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO saveUser(User user) {
        return Mapper.toDTO(userRepository.save(user));
    }

    public UserDTO updateUser(Long id, User userUpdated) {
        User userToUpdate = userRepository.findById(id).get();

        userToUpdate.setName(userUpdated.getName());
        userToUpdate.setTasks(userToUpdate.getTasks());

        userRepository.save(userToUpdate);
        return Mapper.toDTO(userToUpdate);
    }
}
