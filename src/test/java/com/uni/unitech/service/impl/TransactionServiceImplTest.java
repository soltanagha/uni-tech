package com.uni.unitech.service.impl;

import com.uni.unitech.dto.AccountDto;
import com.uni.unitech.dto.CurrencyRateDto;
import com.uni.unitech.dto.TransactionDto;
import com.uni.unitech.dto.mapper.AccountMapper;
import com.uni.unitech.entity.Account;
import com.uni.unitech.entity.User;
import com.uni.unitech.enumerated.CurrencyTypes;
import com.uni.unitech.enumerated.Status;
import com.uni.unitech.exception.BadRequestException;
import com.uni.unitech.repository.TransactionRepository;
import com.uni.unitech.service.AccountService;
import com.uni.unitech.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class TransactionServiceImplTest {

    @Mock
    private AccountService accountService;

    @Mock
    private UserService userService;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CurrencyRateServiceImpl currencyRateService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test1", "123"));

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransaction_sameAccountTransfer() {
        TransactionDto dto = new TransactionDto();
        dto.setFromAccountNumber("123");
        dto.setToAccountNumber("123");

        assertThrows(BadRequestException.class, () -> transactionService.createTransaction(dto));
    }

    @Test
    void createTransaction_successful() {

        TransactionDto dto = new TransactionDto();
        dto.setFromAccountNumber("123");
        dto.setToAccountNumber("456");
        dto.setAmount(50.0);

        User user = User.builder().id(1).pin("test1").password("123").build();
        User user2 = User.builder().id(2).pin("test2").password("123").build();

        when(userService.getUserByPin(user.getPin())).thenReturn(Optional.of(user));
        when(userService.getUserByPin(user2.getPin())).thenReturn(Optional.of(user2));
        AccountDto fromAccountDto = AccountDto.builder().user(user).accountNumber("123").status(Status.ACTIVE).balance(100.00).build();
        AccountDto toAccountDto =  AccountDto.builder().user(user2).accountNumber("456").status(Status.ACTIVE).balance(150.00).build();

        Account fromAccount = Account.builder().user(user).accountNumber("123").status(Status.ACTIVE).balance(100.00).build();
        Account toAccount =  Account.builder().user(user2).accountNumber("456").status(Status.ACTIVE).balance(150.00).build();

        when(accountMapper.toEntity(fromAccountDto)).thenReturn(fromAccount);
        when(accountMapper.toDto(fromAccount)).thenReturn(fromAccountDto);
        when(accountMapper.toEntity(toAccountDto)).thenReturn(toAccount);
        when(accountMapper.toDto(toAccount)).thenReturn(toAccountDto);

        CurrencyRateDto currencyRateDto = new CurrencyRateDto(CurrencyTypes.USD, CurrencyTypes.USD, 1.0);

        when(accountService.getAccountByNumber("123")).thenReturn(Optional.of(fromAccountDto));
        when(accountService.getAccountByNumber("456")).thenReturn(Optional.of(toAccountDto));
        when(currencyRateService.getCurrencyRate(any())).thenReturn(Optional.of(currencyRateDto));

        TransactionDto result = transactionService.createTransaction(dto);

        assertEquals(dto, result);
    }

}
