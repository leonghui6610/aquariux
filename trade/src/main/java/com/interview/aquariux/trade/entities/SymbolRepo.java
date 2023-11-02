package com.interview.aquariux.trade.entities;

import com.interview.aquariux.trade.repo.BaseRepo;

import java.util.List;

public interface SymbolRepo extends BaseRepo<Symbol, Long> {
    List<Symbol> findAllByAllowedTradeTrue();

    Symbol findByName(String name);
}
