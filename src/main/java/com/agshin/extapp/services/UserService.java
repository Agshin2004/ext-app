package com.agshin.extapp.services;

import com.agshin.extapp.exceptions.DataExistsException;
import com.agshin.extapp.mappers.UserMapper;
import com.agshin.extapp.model.entities.User;
import com.agshin.extapp.model.request.user.CreateUserRequest;
import com.agshin.extapp.model.response.user.UserResponse;
import com.agshin.extapp.repositories.UserRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserResponse createUser(CreateUserRequest request) {
        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    throw new DataExistsException("Email already exists");
                });

        userRepository.findByUsername(request.username())
                .ifPresent(user -> {
                    throw new DataExistsException("Email already exists");
                });

        User user = userMapper.toEntity(request);

        userRepository.save(user);

        UserResponse response = userMapper.toResponse(user);

        return response;
    }
}
