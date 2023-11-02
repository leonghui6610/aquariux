package com.interview.aquariux.trade.entities;

import com.interview.aquariux.trade.repo.BaseRepo;

public interface WalletRepo extends BaseRepo<Wallet, Long> {
    Wallet findByAddress(String address);
}
