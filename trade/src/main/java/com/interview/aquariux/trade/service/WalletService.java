package com.interview.aquariux.trade.service;

import com.interview.aquariux.trade.dtos.CryptoBalance;
import com.interview.aquariux.trade.dtos.WalletResponse;
import com.interview.aquariux.trade.entities.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WalletService extends BaseServiceSupport<Wallet, Long, WalletRepo> {

    @Autowired
    TraderRepo traderRepo;

    @Autowired
    CryptoHoldingsRepo cryptoRepo;

    @Autowired
    SymbolRepo symbolRepo;

    public WalletResponse getWalletIInfo(Long traderId) {
        WalletResponse resp = new WalletResponse();
        Optional<Trader> t = traderRepo.findById(traderId);
        if (t.isEmpty()) {
            resp.setStatusMsg("Trader id " + traderId + " not found.");
        } else {
            Trader trader = t.get();
            resp.setName(trader.getName());
            Wallet w = repo.findByAddress(trader.getWalletAdd());
            if (w == null) {
                resp.setStatusMsg("Wallet address " + trader.getWalletAdd() + " not found.");
            } else {
                resp.setWalletAddress(w.getAddress());
                resp.setUsdtBalance(w.getUsdtBalance());
                List<CryptoHoldings> chList = cryptoRepo.findByWalletAddressAndQtyGreaterThan(w.getAddress(), BigDecimal.ZERO);
                List<CryptoBalance> balance = chList.stream().map(c -> {
                    CryptoBalance cb = new CryptoBalance();
                    cb.setCrypto(c.getSymbol());
                    cb.setUnits(c.getQty());
                    cb.setUnitPrice(c.getUsdtPerUnit());
                    Symbol x = symbolRepo.findByName(c.getSymbol());
                    if (x != null) {
                        cb.setCurrentUnitPrice(x.getBidPrice());
                    }
                    return cb;
                }).collect(Collectors.toList());
                resp.setCrypto(balance);
            }

        }
        return resp;
    }
}
