package com.agshin.extapp.services;

import com.agshin.extapp.exceptions.DataExistsException;
import com.agshin.extapp.exceptions.DataNotFoundException;
import com.agshin.extapp.exceptions.InvalidTokenException;
import com.agshin.extapp.exceptions.UnauthorizedException;
import com.agshin.extapp.mappers.UserMapper;
import com.agshin.extapp.model.entities.PasswordResetToken;
import com.agshin.extapp.model.entities.User;
import com.agshin.extapp.model.request.user.CreateUserRequest;
import com.agshin.extapp.model.response.user.UserResponse;
import com.agshin.extapp.repositories.PasswordResetTokenRepository;
import com.agshin.extapp.repositories.UserRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
import com.agshin.extapp.utils.JwtUtils;
import jakarta.validation.constraints.Email;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;
    private PasswordResetTokenRepository tokenRepository;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            JwtUtils jwtUtils,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            PasswordResetTokenRepository tokenRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
        this.tokenRepository = tokenRepository;
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

        emailService.sendPasswordResetEmail("nadirov.main@gmail.com", "hello");

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Passwords do not match");
        }

        String jwt = jwtUtils.generateToken(user);

        return userMapper.toResponse(user, jwt);
    }

    public void forgotPassword(@Email String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    String rawToken = UUID.randomUUID().toString();
                    String tokenHash = passwordEncoder.encode(rawToken);

                    PasswordResetToken token = new PasswordResetToken();
                    token.setTokenHash(tokenHash);
                    token.setExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS));
                    tokenRepository.save(token);

                    emailService.sendPasswordResetEmail(
                            user.getEmail(),
                            rawToken
                    );
                });
    }

    public void resetPassword(String rawToken, String newPassword) {
        PasswordResetToken token = tokenRepository
                .findValidTokens(Instant.now())
                .stream()
                .filter(t -> passwordEncoder.matches(rawToken, t.getTokenHash()))
                .findFirst()
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));

        token.setUsed(true);

        userRepository.save(user);
        tokenRepository.save(token);
    }
}
