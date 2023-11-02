package com.interview.aquariux.trade.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CryptoBalance {
    String crypto;
    BigDecimal units;
    BigDecimal unitPrice;
    BigDecimal currentUnitPrice;
}
