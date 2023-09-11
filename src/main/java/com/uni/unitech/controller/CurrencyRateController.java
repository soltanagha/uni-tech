package com.uni.unitech.controller;

import com.uni.unitech.dto.CurrencyRateDto;
import com.uni.unitech.dto.Response;
import com.uni.unitech.enumerated.CurrencyTypes;
import com.uni.unitech.service.CurrencyRateService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyRateController {

    private final CurrencyRateService currencyRateService;

    @GetMapping
    public ResponseEntity<Response> getCurrencyRate(@RequestParam @NotBlank CurrencyTypes currencyFrom, @RequestParam @NotBlank CurrencyTypes currencyTo) {
        var currencyRtoDto = CurrencyRateDto.builder().currencyFrom(currencyFrom).currencyTo(currencyTo).build();
        return ResponseEntity.ok(Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("currency-rate",currencyRateService.getCurrencyRate(currencyRtoDto)))
                .message("Currency rate retrieved!")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build());
    }

}
