package com.uni.unitech.service;

import com.uni.unitech.dto.AccountDto;
import com.uni.unitech.entity.User;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<AccountDto> getAllAccounts();
    List<AccountDto> getActiveAccounts();
    Optional<AccountDto> getAccountByNumber(String number);
    AccountDto saveAccount(AccountDto accountDto);
    void deleteAccount(Long id);

    List<AccountDto> createInitialBalance(User user);
}
