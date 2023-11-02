package com.interview.aquariux.trade.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SymbolDto {
    String symbol;
    BigDecimal bidPrice, bidQty, askPrice, askQty;
}
