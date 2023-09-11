package com.uni.unitech.service.impl;

import com.uni.unitech.dto.AccountDto;
import com.uni.unitech.dto.CurrencyRateDto;
import com.uni.unitech.dto.TransactionDto;
import com.uni.unitech.dto.mapper.AccountMapper;
import com.uni.unitech.entity.Account;
import com.uni.unitech.entity.Transaction;
import com.uni.unitech.entity.User;
import com.uni.unitech.enumerated.Status;
import com.uni.unitech.exception.BadRequestException;
import com.uni.unitech.exception.ForbiddenException;
import com.uni.unitech.exception.NotFoundException;
import com.uni.unitech.exception.UnauthorizedException;
import com.uni.unitech.exception.util.ExceptionCodes;
import com.uni.unitech.repository.TransactionRepository;
import com.uni.unitech.service.AccountService;
import com.uni.unitech.service.CurrencyRateService;
import com.uni.unitech.service.TransactionService;
import com.uni.unitech.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final CurrencyRateService currencyRateService;


    @Override
    @Transactional
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        validateSameAccountTransfer(transactionDto);

        var currentUser = getCurrentUser();

        var fromAccount = getAccount(transactionDto.getFromAccountNumber(), currentUser);
        var toAccount = getAccount(transactionDto.getToAccountNumber());

        checkAccountStatus(fromAccount, toAccount);
        checkSufficientBalance(fromAccount, transactionDto.getAmount());

        double currencyRate = getCurrencyConversionRate(fromAccount, toAccount);
        double transferAmount = transactionDto.getAmount() * currencyRate;

        performTransaction(fromAccount, toAccount, transactionDto.getAmount(), transferAmount);

        return transactionDto;
    }

    private void validateSameAccountTransfer(TransactionDto transactionDto) {
        if (Objects.equals(transactionDto.getFromAccountNumber(), transactionDto.getToAccountNumber())) {
            throw new BadRequestException(ExceptionCodes.TRANSFER_BETWEEN_SAME_ACCOUNT);
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException(ExceptionCodes.INVALID_TOKEN,
                    new ForbiddenException(ExceptionCodes.METHOD_ARGUMENT_NOT_VALID));
        }

        var userOpt = userService.getUserByPin(authentication.getName());
        return userOpt.orElseThrow(() -> new BadRequestException(ExceptionCodes.USER_NOT_FOUND));
    }

    private Account getAccount(String accountNumber, User... user) {
        var accountOpt = accountService.getAccountByNumber(accountNumber);
        AccountDto accountDto = accountOpt.orElseThrow(() -> new NotFoundException(ExceptionCodes.INVALID_ACCOUNT_NUMBER));
        if (user.length > 0 && !Objects.equals(accountDto.getUser().getId(), user[0].getId())) {
            throw new BadRequestException(ExceptionCodes.INVALID_ACCOUNT_NUMBER);
        }
        return accountMapper.toEntity(accountDto);
    }

    private void checkAccountStatus(Account fromAccount, Account toAccount) {
        if (fromAccount.getStatus() == Status.INACTIVE || toAccount.getStatus() == Status.INACTIVE) {
            throw new ForbiddenException(ExceptionCodes.INACTIVE_ACCOUNT);
        }
    }

    private void checkSufficientBalance(Account fromAccount, double amount) {
        if (fromAccount.getBalance() < amount) {
            throw new ForbiddenException(ExceptionCodes.INSUFFICIENT_BALANCE);
        }
    }

    private double getCurrencyConversionRate(Account fromAccount, Account toAccount) {
        if (fromAccount.getCurrency() != toAccount.getCurrency()) {
            var currencyRateDto = CurrencyRateDto.builder()
                    .currencyFrom(fromAccount.getCurrency())
                    .currencyTo(toAccount.getCurrency())
                    .build();

            var currencyRateDtoOpt = currencyRateService.getCurrencyRate(currencyRateDto);
            currencyRateDto = currencyRateDtoOpt.orElseThrow(() -> new BadRequestException(ExceptionCodes.CURRENCY_RATE_NOT_FOUND));
            return currencyRateDto.getRate();
        }
        return 1;
    }

    private void performTransaction(Account fromAccount, Account toAccount, double originalAmount, double convertedAmount) {
        fromAccount.setBalance(fromAccount.getBalance() - originalAmount);
        toAccount.setBalance(toAccount.getBalance() + convertedAmount);

        accountService.saveAccount(accountMapper.toDto(fromAccount));
        accountService.saveAccount(accountMapper.toDto(toAccount));


        var transaction = Transaction.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(originalAmount)
                .currencyRate(convertedAmount/originalAmount)
                .build();

        transactionRepository.save(transaction);
    }
}
