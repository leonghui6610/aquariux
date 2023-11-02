package com.interview.aquariux.trade.service;

import com.interview.aquariux.trade.entities.TxnHistory;
import com.interview.aquariux.trade.entities.TxnHistoryRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TxnHistoryService extends BaseServiceSupport<TxnHistory, Long, TxnHistoryRepo> {

    public List<TxnHistory> getTxnHistory(Long traderId) {
        return repo.findByTraderIdOrderByCreatedDesc(traderId);
    }
}
