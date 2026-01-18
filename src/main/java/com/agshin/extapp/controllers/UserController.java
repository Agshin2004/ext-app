package com.agshin.extapp.controllers;

import com.agshin.extapp.model.constants.ApplicationConstants;
import com.agshin.extapp.model.request.user.CreateUserRequest;
import com.agshin.extapp.model.request.user.SignInUserRequest;
import com.agshin.extapp.model.response.GenericResponse;
import com.agshin.extapp.model.response.user.UserResponse;
import com.agshin.extapp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        var genericResponse = GenericResponse.create(ApplicationConstants.SUCCESS, userResponse, HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(genericResponse);
    }

}
