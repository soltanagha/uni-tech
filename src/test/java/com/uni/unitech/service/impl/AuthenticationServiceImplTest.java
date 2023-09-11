package com.uni.unitech.service.impl;

import com.uni.unitech.auth.dto.request.SignUpRequest;
import com.uni.unitech.auth.dto.request.SigninRequest;
import com.uni.unitech.auth.dto.response.JwtAuthenticationResponse;
import com.uni.unitech.entity.User;
import com.uni.unitech.exception.ForbiddenException;
import com.uni.unitech.exception.NotFoundException;
import com.uni.unitech.repository.UserRepository;
import com.uni.unitech.service.AccountService;
import com.uni.unitech.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signup_withDuplicatePin() {
        SignUpRequest request = new SignUpRequest("1234", "password");
        when(userRepository.findByPin("1234")).thenReturn(Optional.of(new User()));

        assertThrows(ForbiddenException.class, () -> authenticationService.signup(request));
    }

    @Test
    void signup_successfully() {
        SignUpRequest request = new SignUpRequest("1234", "password");
        when(userRepository.findByPin("1234")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        String result = authenticationService.signup(request);

        assertEquals("Account registered!", result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void signin_userNotFound() {
        SigninRequest request = new SigninRequest("1234", "password");
        when(userRepository.findByPin("1234")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authenticationService.signin(request));
    }

    @Test
    void signin_successfully() {
        SigninRequest request = new SigninRequest("1234", "password");
        User user = new User();
        when(userRepository.findByPin("1234")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("mockedToken");

        JwtAuthenticationResponse response = authenticationService.signin(request);

        assertEquals("mockedToken", response.getToken());
        verify(accountService, times(1)).createInitialBalance(user);
    }

}
