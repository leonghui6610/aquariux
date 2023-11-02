package com.interview.aquariux.trade.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class WalletResponse {
    String name, statusMsg;
    String walletAddress;
    BigDecimal usdtBalance;
    List<CryptoBalance> crypto;
}
