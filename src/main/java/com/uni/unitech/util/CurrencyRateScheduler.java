package com.uni.unitech.util;

import com.uni.unitech.dto.CurrencyRateDto;
import com.uni.unitech.enumerated.CurrencyTypes;
import com.uni.unitech.service.CurrencyRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyRateScheduler {

    private final CurrencyRateService currencyRateService;

    private final Random random = new Random();

    @Scheduled(fixedRate = 60000)
    public void generateRandomCurrencyRate() {
        CurrencyTypes[] currencies = CurrencyTypes.values();

        for (CurrencyTypes from : currencies) {
            for (CurrencyTypes to : currencies) {
                if (from != to) {
                    var rate = CurrencyRateDto.builder()
                            .currencyFrom(from)
                            .currencyTo(to)
                            .rate(random.nextDouble() * (2.5 -0.5) + 0.5).build();

                    rate = currencyRateService.create(rate);
                    log.info(rate.toString());
                }
            }
        }
    }
}
