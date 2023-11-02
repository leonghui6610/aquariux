package com.interview.aquariux.trade.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TradeResponse {
    String statusMsg;
    String traderName;
    BigDecimal usdtBalance;
    List<CryptoBalance> cryptos;
}
