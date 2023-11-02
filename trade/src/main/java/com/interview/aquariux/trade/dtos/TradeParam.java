package com.interview.aquariux.trade.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TradeParam {
    Long traderId;
    String symbol;
    BigDecimal unit;
    BuySell direction;
}
