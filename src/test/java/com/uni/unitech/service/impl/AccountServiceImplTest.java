package com.uni.unitech.service.impl;

import com.uni.unitech.dto.AccountDto;
import com.uni.unitech.dto.mapper.AccountMapper;
import com.uni.unitech.entity.Account;
import com.uni.unitech.entity.User;
import com.uni.unitech.enumerated.Status;
import com.uni.unitech.exception.BadRequestException;
import com.uni.unitech.exception.UnauthorizedException;
import com.uni.unitech.exception.util.ExceptionCodes;
import com.uni.unitech.repository.AccountRepository;
import com.uni.unitech.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAccounts() {
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(accountService.getAllAccounts().isEmpty());
    }

    @Test
    void getActiveAccounts_withValidAuthentication() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("validName");
        SecurityContextHolder.getContext().setAuthentication(auth);

        User user = new User();
        when(userRepository.findByPin("validName")).thenReturn(Optional.of(user));
        when(accountRepository.findByUserAndStatus(user, Status.ACTIVE)).thenReturn(Collections.emptyList());

        assertTrue(accountService.getActiveAccounts().isEmpty());
    }

    @Test
    void getActiveAccounts_withAnonymousAuthentication() {
        Authentication auth = mock(AnonymousAuthenticationToken.class);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(UnauthorizedException.class, () -> accountService.getActiveAccounts());
    }

    @Test
    void getAccountByNumber() {
        when(accountRepository.findByAccountNumber("number")).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            accountService.getAccountByNumber("number");
        });

        assertEquals(ExceptionCodes.USER_NOT_FOUND.getExceptionMessage(), exception.getMessage());
    }

    @Test
    void saveAccount() {
        AccountDto accountDto = new AccountDto();
        Account account = new Account();

        when(accountMapper.toEntity(accountDto)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        assertEquals(accountDto, accountService.saveAccount(accountDto));
    }

    @Test
    void deleteAccount() {
        doNothing().when(accountRepository).deleteById(1L);
        accountService.deleteAccount(1L);
        verify(accountRepository, times(1)).deleteById(1L);
    }

    @Test
    void createInitialBalance() {
        User user = new User();
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        var accounts = accountService.createInitialBalance(user);

        assertEquals(3, accounts.size());
    }

    @Test
    void generateAccountNumber() {
        String accountNumber = AccountServiceImpl.generateAccountNumber();
        assertNotNull(accountNumber);
        assertFalse(accountNumber.contains("-"));
    }
}
