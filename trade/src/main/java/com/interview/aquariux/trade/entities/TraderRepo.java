package com.interview.aquariux.trade.entities;

import com.interview.aquariux.trade.repo.BaseRepo;

public interface TraderRepo extends BaseRepo<Trader, Long> {
    Trader findByEmail(String email);
}
