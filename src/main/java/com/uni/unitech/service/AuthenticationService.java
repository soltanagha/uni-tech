package com.uni.unitech.service;

import com.uni.unitech.auth.dto.request.SignUpRequest;
import com.uni.unitech.auth.dto.request.SigninRequest;
import com.uni.unitech.auth.dto.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    String signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}
