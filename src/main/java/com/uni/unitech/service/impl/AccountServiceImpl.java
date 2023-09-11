package com.uni.unitech.service.impl;

import com.uni.unitech.dto.AccountDto;
import com.uni.unitech.dto.mapper.AccountMapper;
import com.uni.unitech.entity.User;
import com.uni.unitech.enumerated.CurrencyTypes;
import com.uni.unitech.enumerated.Status;
import com.uni.unitech.exception.BadRequestException;
import com.uni.unitech.exception.ForbiddenException;
import com.uni.unitech.exception.UnauthorizedException;
import com.uni.unitech.exception.util.ExceptionCodes;
import com.uni.unitech.repository.AccountRepository;
import com.uni.unitech.repository.UserRepository;
import com.uni.unitech.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Override
    public List<AccountDto> getAllAccounts() {
        return accountMapper.toDtoList(accountRepository.findAll());
    }

    @Override
    public List<AccountDto> getActiveAccounts() {
        String currentUserName = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        } else
            throw new UnauthorizedException(ExceptionCodes.INVALID_TOKEN,
                    new ForbiddenException(ExceptionCodes.METHOD_ARGUMENT_NOT_VALID));

        var userOpt = userRepository.findByPin(currentUserName);
        if (userOpt.isEmpty()) {
            throw new BadRequestException(ExceptionCodes.USER_NOT_FOUND);
        }
        var user = userOpt.get();
        var accounts = accountRepository.findByUserAndStatus(user,Status.ACTIVE);
        return accountMapper.toDtoList(accounts);
    }

    @Override
    public Optional<AccountDto> getAccountByNumber(String number) {
        var accountOpt = accountRepository.findByAccountNumber(number);
        if (accountOpt.isEmpty()) {
            throw new BadRequestException(ExceptionCodes.INVALID_ACCOUNT_NUMBER);
        }
        var account  = accountOpt.get();

        return Optional.ofNullable(accountMapper.toDto(account));
    }

    @Override
    public AccountDto saveAccount(AccountDto accountDto) {
        var account = accountMapper.toEntity(accountDto);
        accountRepository.save(account);
        return accountMapper.toDto(account);
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public List<AccountDto> createInitialBalance(User user) {
        CurrencyTypes[] currencyTypes = {CurrencyTypes.AZN, CurrencyTypes.USD, CurrencyTypes.EUR};
        List<AccountDto> accounts = new ArrayList<>();
        for (CurrencyTypes currencyType: currencyTypes) {
            var accountNumber = generateAccountNumber();
            var newAccountDto = AccountDto.builder().user(user).currency(currencyType).balance(1000.00)
                    .accountNumber(accountNumber).status(Status.ACTIVE).build();

            saveAccount(newAccountDto);
            accounts.add(newAccountDto);
        }

        return accounts;
    }

    public static String generateAccountNumber() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").toUpperCase();
    }

}
