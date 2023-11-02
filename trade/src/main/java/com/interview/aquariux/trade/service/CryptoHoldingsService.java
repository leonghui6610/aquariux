package com.interview.aquariux.trade.service;

import com.interview.aquariux.trade.dtos.BuySell;
import com.interview.aquariux.trade.dtos.CryptoBalance;
import com.interview.aquariux.trade.dtos.TradeParam;
import com.interview.aquariux.trade.dtos.TradeResponse;
import com.interview.aquariux.trade.entities.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CryptoHoldingsService extends BaseServiceSupport<CryptoHoldings, Long, CryptoHoldingsRepo> {
    @Autowired
    SymbolRepo symbolRepo;

    @Autowired
    TraderRepo traderRepo;

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    TxnHistoryRepo txnRepo;

    public TradeResponse executeTrade(TradeParam param) {
        TradeResponse resp = new TradeResponse();
        resp.setStatusMsg("SUCCESS");
        Optional<Trader> t = traderRepo.findById(param.getTraderId());
        if (t.isEmpty()) {
            resp.setStatusMsg("Trader id " + param.getTraderId() + " not found.");
        } else {
            Trader trader = t.get();
            Wallet w = walletRepo.findByAddress(trader.getWalletAdd());
            if (w == null) {
                resp.setStatusMsg("Wallet address " + trader.getWalletAdd() + " not found.");
            } else {
                Symbol s = symbolRepo.findByName(param.getSymbol());
                if (s == null) {
                    resp.setStatusMsg("Symbol " + param.getSymbol() + " not found.");
                } else {
                    boolean tradeSuccess = false;
                    if (param.getDirection().equals(BuySell.BUY)) {
                        tradeSuccess = processBuy(w, param.getUnit(), s, trader);
                    } else {
                        tradeSuccess = processSell(w, param.getUnit(), s, trader);
                    }
                    if (!tradeSuccess) {
                        resp.setStatusMsg("Trade was not carried out.");
                    }
                    resp.setTraderName(trader.getName());
                    resp.setUsdtBalance(w.getUsdtBalance());
                    List<CryptoHoldings> chList = repo.findByWalletAddressAndQtyGreaterThan(w.getAddress(), BigDecimal.ZERO);
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
                    resp.setCryptos(balance);
                }
            }
        }
        return resp;
    }

    @Transactional
    private boolean processBuy(Wallet w, BigDecimal units, Symbol s, Trader t) {
        boolean status = false;
        BigDecimal unitBuyPrice = s.getAskPrice();
        BigDecimal cost = units.multiply(unitBuyPrice);
        if (cost.compareTo(w.getUsdtBalance()) < 1) {
            log.info("Buy processed for wallet {} latest USDT balance {}", w.getAddress(), w.getUsdtBalance());
            CryptoHoldings ch = repo.findByWalletAddressAndSymbolAndUsdtPerUnit(w.getAddress(), s.getName(), unitBuyPrice);
            if (ch != null) {
                ch.setQty(ch.getQty().add(units));
                log.info("Buy process Exist wallet {} holding {} units of {} at unitPrice {} USDT", w.getAddress(), ch.getQty(), s.getName(), ch.getUsdtPerUnit());
            } else {
                ch = new CryptoHoldings();
                ch.setInWallet(true);
                ch.setSymbol(s.getName());
                ch.setQty(units);
                ch.setWalletId(w.getId());
                ch.setUsdtPerUnit(unitBuyPrice);
                ch.setCreated(Instant.now());
                ch.setWalletAddress(w.getAddress());
                log.info("Buy process Create wallet {} holding {} units of {} at unitPrice {} USDT", w.getAddress(), ch.getQty(), s.getName(), ch.getUsdtPerUnit());
            }
            repo.save(ch);
            w.setUsdtBalance(w.getUsdtBalance().subtract(cost));
            walletRepo.save(w);
            TxnHistory txn = new TxnHistory();
            txn.setTraderId(t.getId());
            txn.setQty(units);
            txn.setDirection(BuySell.BUY);
            txn.setSymbol(s.getName());
            txn.setWalletId(w.getId());
            txn.setPerUnitPrice(unitBuyPrice);
            txn.setUsdtValue(BigDecimal.ZERO.subtract(cost));
            txn.setCreated(Instant.now());
            txnRepo.save(txn);
            status = true;
        }
        return status;
    }

    @Transactional
    private boolean processSell(Wallet w, BigDecimal units, Symbol s, Trader t) {
        boolean status = false;
        BigDecimal unitSellPrice = s.getBidPrice();
        List<CryptoHoldings> existingHoldings = repo.findByWalletAddressAndSymbolAndQtyGreaterThan(w.getAddress(), s.getName(), BigDecimal.ZERO);

        if (CollectionUtils.isNotEmpty(existingHoldings)) {
            BigDecimal totalUnits = existingHoldings.stream().map(CryptoHoldings::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            log.info("Processing Sell wallet {} Symbol {} TotalUnits {}", w.getAddress(), s.getName(), units);
            if (totalUnits.compareTo(units) < 0) {
                return false;
            }
            BigDecimal sellProfit = units.multiply(unitSellPrice);
            BigDecimal progCount = units;
            for (CryptoHoldings c : existingHoldings) {
                if (progCount.compareTo(BigDecimal.ZERO) != 0) {
                    if (c.getQty().compareTo(progCount) < 1) {
                        progCount = progCount.subtract(c.getQty());
                        c.setQty(BigDecimal.ZERO);
                    } else {
                        c.setQty(c.getQty().subtract(progCount));
                        progCount = BigDecimal.ZERO;
                    }
                }
            }
            repo.saveAll(existingHoldings);
            w.setUsdtBalance(w.getUsdtBalance().add(sellProfit));
            walletRepo.save(w);
            TxnHistory txn = new TxnHistory();
            txn.setTraderId(t.getId());
            txn.setQty(units);
            txn.setDirection(BuySell.SELL);
            txn.setSymbol(s.getName());
            txn.setWalletId(w.getId());
            txn.setPerUnitPrice(unitSellPrice);
            txn.setUsdtValue(sellProfit);
            txn.setCreated(Instant.now());
            txnRepo.save(txn);
            status = true;
        }
        return status;
    }

}
