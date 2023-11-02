package com.interview.aquariux.trade.controller;


import com.interview.aquariux.trade.dtos.TradeParam;
import com.interview.aquariux.trade.dtos.TradeResponse;
import com.interview.aquariux.trade.dtos.WalletResponse;
import com.interview.aquariux.trade.entities.Symbol;
import com.interview.aquariux.trade.entities.TxnHistory;
import com.interview.aquariux.trade.service.CryptoHoldingsService;
import com.interview.aquariux.trade.service.SymbolService;
import com.interview.aquariux.trade.service.TxnHistoryService;
import com.interview.aquariux.trade.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crypto")
public class TradeController {

    @Autowired
    SymbolService symService;

    @Autowired
    CryptoHoldingsService cryptoService;

    @Autowired
    WalletService walletService;

    @Autowired
    TxnHistoryService txnService;

    /*Create an api to retrieve the latest best aggregated price.
    http://localhost:8008/crypto/getPrices
     */
    @GetMapping("/getPrices")
    public List<Symbol> listProduct() {
        return symService.getAllowedProduct();
    }

    /*
    Create an api which allows users to trade based on the latest best aggregated price.
    http://localhost:8008/crypto/trade
    Trader Id 1 was already created at DataInitilizer class upon server startup
    POST body sample 1
        {
            "traderId":1,"symbol":"BTCUSDT","unit":0.1,"direction":"BUY"
        }
        sample 2
        {
            "traderId":1,"symbol":"BTCUSDT","unit":0.1,"direction":"SELL"
        }
     */
    @PostMapping("/trade")
    public TradeResponse submitTrade(@RequestBody TradeParam param) {
        return cryptoService.executeTrade(param);
    }

    /*
    Create an api to retrieve the user’s crypto currencies wallet balance
    http://localhost:8008/crypto/trade?traderId=1
     */
    @GetMapping("/wallet")
    public WalletResponse getWallet(Long traderId) {
        return walletService.getWalletIInfo(traderId);
    }

    /*
    Create an api to retrieve the user trading history.
    http://localhost:8008/crypto/history?traderId=1
     */

    @GetMapping("/history")
    public List<TxnHistory> getHistory(Long traderId) {
        return txnService.getTxnHistory(traderId);
    }
}
