package com.uni.unitech.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    @NotBlank
    private String fromAccountNumber;
    @NotBlank
    private String toAccountNumber;
    @Min(value = 1,message = "Minimum transfer value is 1")
    private Double amount;
}
