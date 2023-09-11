package com.uni.unitech.repository;

import com.uni.unitech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPin(String pin);
}
