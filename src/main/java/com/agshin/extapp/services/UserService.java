package com.agshin.extapp.services;

import com.agshin.extapp.exceptions.DataExistsException;
import com.agshin.extapp.exceptions.DataNotFoundException;
import com.agshin.extapp.exceptions.UnauthorizedException;
import com.agshin.extapp.mappers.UserMapper;
import com.agshin.extapp.model.entities.User;
import com.agshin.extapp.model.request.user.CreateUserRequest;
import com.agshin.extapp.model.response.user.UserResponse;
import com.agshin.extapp.repositories.UserRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
import com.agshin.extapp.utils.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;

    public UserService(UserRepository userRepository, UserMapper userMapper, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
    }

    public UserResponse createUser(String email, String username, String password) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new DataExistsException("Email already exists");
                });

        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new DataExistsException("Email already exists");
                });

        User user = userMapper.toEntity(email, username, password);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        String jwt = jwtUtils.generateToken(user);

        return userMapper.toResponse(user, jwt);
    }

    public UserResponse getJwtForCreds(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User with email not found: " + email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Passwords do not match");
        }

        String jwt = jwtUtils.generateToken(user);

        return userMapper.toResponse(user, jwt);
    }
}
