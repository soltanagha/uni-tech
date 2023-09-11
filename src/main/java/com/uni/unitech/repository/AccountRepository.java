package com.uni.unitech.repository;

import com.uni.unitech.entity.Account;
import com.uni.unitech.entity.User;
import com.uni.unitech.enumerated.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserAndStatus(User user, Status status);
    Optional<Account> findByAccountNumber(String accountNumber);
}
