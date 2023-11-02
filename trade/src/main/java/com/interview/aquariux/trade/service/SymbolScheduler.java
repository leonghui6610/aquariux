package com.interview.aquariux.trade.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SymbolScheduler {
    @Autowired
    SymbolService symbolService;

    /*

    Price aggregation from the source below:
    Binance
    Url : https://api.binance.com/api/v3/ticker/bookTicker
    Houbi
    Url : https://api.huobi.pro/market/tickers
    Create a 10 seconds interval scheduler to retrieve the pricing from the source
    above and store the best pricing into the database.
    Hints: Bid Price use for SELL order, Ask Price use for BUY order

     */

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void syncSymbols() {
        symbolService.refreshSymbols();
    }
}
