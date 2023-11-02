package com.interview.aquariux.trade.service;

import com.interview.aquariux.trade.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    SymbolRepo symbRepo;

    @Autowired
    WalletRepo wallRepo;

    @Autowired
    TraderRepo traderRepo;

    @Override
    public void run(ApplicationArguments args) {
        if (symbRepo.findByName("BTCUSDT") == null) {
            Symbol btcUSDT = new Symbol();
            btcUSDT.setName("BTCUSDT");
            btcUSDT.setAllowedTrade(true);
            btcUSDT.setBidPrice(BigDecimal.ZERO);
            btcUSDT.setAskQty(BigDecimal.ZERO);
            btcUSDT.setBidQty(BigDecimal.ZERO);
            btcUSDT.setAskPrice(new BigDecimal(100000));
            btcUSDT.setCreated(Instant.now());
            symbRepo.save(btcUSDT);
        }
        if (symbRepo.findByName("ETHUSDT") == null) {
            Symbol ethUSDT = new Symbol();
            ethUSDT.setName("ETHUSDT");
            ethUSDT.setAllowedTrade(true);
            ethUSDT.setBidPrice(BigDecimal.ZERO);
            ethUSDT.setAskQty(BigDecimal.ZERO);
            ethUSDT.setBidQty(BigDecimal.ZERO);
            ethUSDT.setAskPrice(new BigDecimal(100000));
            ethUSDT.setCreated(Instant.now());
            symbRepo.save(ethUSDT);
        }

        Wallet w = new Wallet();
        if (wallRepo.findByAddress("633d6a3c5a012ccde48c43a18a4576ea") == null) {

            w.setCreated(Instant.now());
            w.setAddress("633d6a3c5a012ccde48c43a18a4576ea");
            w.setUsdtBalance(new BigDecimal(50000));
            wallRepo.save(w);
        }

        if (traderRepo.findByEmail("TraderSam@gmail.com") == null) {
            Trader traderSam = new Trader();
            traderSam.setName("TraderSam");
            traderSam.setEmail("TraderSam@gmail.com");
            traderSam.setWalletId(w.getId());
            traderSam.setWalletAdd("633d6a3c5a012ccde48c43a18a4576ea");
            traderSam.setCreated(Instant.now());
            traderRepo.save(traderSam);
        }

    }
}
