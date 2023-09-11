package com.uni.unitech.service.impl;

import com.uni.unitech.auth.dto.request.SignUpRequest;
import com.uni.unitech.auth.dto.request.SigninRequest;
import com.uni.unitech.auth.dto.response.JwtAuthenticationResponse;
import com.uni.unitech.entity.User;
import com.uni.unitech.enumerated.Role;
import com.uni.unitech.exception.ForbiddenException;
import com.uni.unitech.exception.NotFoundException;
import com.uni.unitech.exception.util.ExceptionCodes;
import com.uni.unitech.repository.UserRepository;
import com.uni.unitech.service.AccountService;
import com.uni.unitech.service.AuthenticationService;
import com.uni.unitech.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AccountService accountService;

    @Override
    public String signup(SignUpRequest request) {

        if (userRepository.findByPin(request.getPin()).isPresent())
            throw new ForbiddenException(ExceptionCodes.DUPLICATE_DATA_EXCEPTION);

        var user = User.builder().pin(request.getPin()).password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER).build();
        userRepository.save(user);

        accountService.createInitialBalance(user);

        return "Account registered!";
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {

        var user = userRepository.findByPin(request.getPin())
                .orElseThrow(() -> new NotFoundException(ExceptionCodes.INVALID_PIN_OR_PASSWORD));

        var jwt = jwtService.generateToken(user);

        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}
