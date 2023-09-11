package com.uni.unitech.service.impl;

import com.uni.unitech.entity.User;
import com.uni.unitech.exception.NotFoundException;
import com.uni.unitech.exception.util.ExceptionCodes;
import com.uni.unitech.repository.UserRepository;
import com.uni.unitech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return pin -> userRepository.findByPin(pin)
                .orElseThrow(() -> new NotFoundException(ExceptionCodes.USER_NOT_FOUND));
    }

    @Override
    public Optional<User> getUserByPin(String pin) {
        return userRepository.findByPin(pin);
    }

}
