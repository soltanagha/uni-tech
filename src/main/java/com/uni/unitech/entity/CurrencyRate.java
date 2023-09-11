package com.uni.unitech.entity;

import com.uni.unitech.enumerated.CurrencyTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "currency_rates")
public class CurrencyRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyTypes currencyFrom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyTypes currencyTo;

    private Double rate;

    @Temporal(TemporalType.TIMESTAMP)
    private Instant creationDateTime;
}