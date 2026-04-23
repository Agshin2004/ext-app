package com.agshin.extapp.features.user.application;

import com.agshin.extapp.features.audit.domain.AuditType;
import com.agshin.extapp.features.audit.infrastructure.annotations.Auditable;
import com.agshin.extapp.features.file.application.EmailService;
import com.agshin.extapp.features.user.api.dto.UserResponse;
import com.agshin.extapp.features.user.domain.PasswordResetToken;
import com.agshin.extapp.features.user.domain.User;
import com.agshin.extapp.features.user.infrastructure.PasswordResetTokenRepository;
import com.agshin.extapp.features.user.infrastructure.UserMapper;
import com.agshin.extapp.features.user.infrastructure.UserRepository;
import com.agshin.extapp.shared.exception.DataExistsException;
import com.agshin.extapp.shared.exception.DataNotFoundException;
import com.agshin.extapp.shared.exception.InvalidTokenException;
import com.agshin.extapp.shared.exception.UnauthorizedException;
//import org.springframework.security.crypto.password.PasswordEncoder;
import com.agshin.extapp.shared.security.JwtUtils;
import jakarta.transaction.Transactional;
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

    @Transactional
    @Auditable(action = AuditType.USER_CREATE, entity = "User")
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

    public void forgotPassword(@Email String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    String rawToken = UUID.randomUUID().toString();
                    String tokenHash = passwordEncoder.encode(rawToken);

                    PasswordResetToken token = new PasswordResetToken();
                    token.setTokenHash(tokenHash);
                    token.setExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS));
                    token.setUser(user);
                    tokenRepository.save(token);

                    emailService.sendPasswordResetEmail(
                            user.getEmail(),
                            rawToken
                    );
                });
    }

    @Transactional
    @Auditable(action = AuditType.STATE_CHANGE, entity = "User")
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
