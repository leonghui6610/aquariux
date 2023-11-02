package com.interview.aquariux.trade.entities;

import com.interview.aquariux.trade.repo.BaseRepo;

import java.util.List;

public interface TxnHistoryRepo extends BaseRepo<TxnHistory, Long> {

    List<TxnHistory> findByTraderIdOrderByCreatedDesc(Long traderId);
}
