package com.uni.unitech.controller;

import com.uni.unitech.auth.dto.request.SignUpRequest;
import com.uni.unitech.auth.dto.request.SigninRequest;
import com.uni.unitech.auth.dto.response.JwtAuthenticationResponse;
import com.uni.unitech.dto.Response;
import com.uni.unitech.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<Response> signup(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("signup", authenticationService.signup(request)))
                .message("New user created!")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("/signin")
    public ResponseEntity<Response> signin(@Valid @RequestBody SigninRequest request) {
        return ResponseEntity.ok(Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("signin", authenticationService.signin(request)))
                .message("Sign in done.")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build());
    }
}
