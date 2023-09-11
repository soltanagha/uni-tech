package com.uni.unitech.service;

import com.uni.unitech.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService {

    UserDetailsService userDetailsService();
    Optional<User> getUserByPin(String pin);

}
