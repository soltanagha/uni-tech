package com.uni.unitech.dto;

import com.uni.unitech.entity.User;
import com.uni.unitech.enumerated.CurrencyTypes;
import com.uni.unitech.enumerated.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private Long id;
    private User user;
    private Double balance;
    private Status status;
    private String accountNumber;
    private CurrencyTypes currency;
}
