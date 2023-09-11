package com.uni.unitech.repository;

import com.uni.unitech.entity.CurrencyRate;
import com.uni.unitech.enumerated.CurrencyTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
    Optional<CurrencyRate> findFirstByCurrencyFromAndCurrencyToOrderByCreationDateTimeDesc(CurrencyTypes currencyFrom, CurrencyTypes currencyTo);

}