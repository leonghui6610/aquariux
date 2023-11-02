package com.interview.aquariux.trade.entities;

import com.interview.aquariux.trade.repo.BaseRepo;

import java.math.BigDecimal;
import java.util.List;

public interface CryptoHoldingsRepo extends BaseRepo<CryptoHoldings, Long> {
    CryptoHoldings findByWalletAddressAndSymbolAndUsdtPerUnit(String walletAddress, String symbol, BigDecimal usdtPerUnit);

    List<CryptoHoldings> findByWalletAddressAndSymbolAndQtyGreaterThan(String walletAddress, String symbol, BigDecimal qty);

    List<CryptoHoldings> findByWalletAddressAndQtyGreaterThan(String walletAddress, BigDecimal qty);
}
