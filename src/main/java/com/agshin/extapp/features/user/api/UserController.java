package com.agshin.extapp.features.user.api;

import com.agshin.extapp.features.user.api.dto.CreateUserRequest;
import com.agshin.extapp.features.user.api.dto.ForgotPasswordRequest;
import com.agshin.extapp.features.user.api.dto.ResetPasswordRequest;
import com.agshin.extapp.features.user.api.dto.SignInUserRequest;
import com.agshin.extapp.features.user.api.dto.UserResponse;
import com.agshin.extapp.features.user.application.UserService;
import com.agshin.extapp.shared.api.ApplicationConstants;
import com.agshin.extapp.shared.api.GenericResponse;
import com.agshin.extapp.shared.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<GenericResponse<UserResponse>> signUp(@Valid @RequestBody CreateUserRequest request) {
        UserResponse userResponse = userService.createUser(
                request.email(),
                request.username(),
                request.password()
        );

        var genericResponse = GenericResponse.create(
                ApplicationConstants.SUCCESS,
                userResponse,
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(genericResponse);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<GenericResponse<UserResponse>> signIn(@Valid @RequestBody SignInUserRequest request) {
        UserResponse userResponse = userService.getJwtForCreds(request.email(), request.password());

        var genericResponse = GenericResponse.create(
                ApplicationConstants.SUCCESS,
                userResponse,
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(genericResponse);
    }

    // generate token, send email
    @PostMapping("/forgot-password")
    public ResponseEntity<GenericResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userService.forgotPassword(userDetails.getEmail());

        GenericResponse<Void> response = GenericResponse.create(
                ApplicationConstants.SUCCESS,
                null,
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<GenericResponse<Void>> resetPassword(@RequestBody ResetPasswordRequest request) {

        userService.resetPassword(request.rawToken(), request.newPassword());

        GenericResponse<Void> response = GenericResponse.create(
                ApplicationConstants.SUCCESS,
                null,
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);

    }
}
