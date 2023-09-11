package com.uni.unitech.dto.mapper;

import com.uni.unitech.dto.AccountDto;
import com.uni.unitech.entity.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toEntity(AccountDto accountDto);

    AccountDto toDto(Account account);

    List<AccountDto> toDtoList(List<Account> accounts);
    List<Account> toEntityList(List<AccountDto> accountDtos);
}
