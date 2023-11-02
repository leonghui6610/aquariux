package com.interview.aquariux.trade.entities;


import com.interview.aquariux.trade.dtos.BuySell;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@Table(name = "tb_txn_history")
public class TxnHistory extends BaseVO {
    @CreatedDate
    Instant created;

    private Long traderId;
    private Long walletId;
    private String symbol;
    private BuySell direction;
    private BigDecimal perUnitPrice;
    private BigDecimal usdtValue;
    private BigDecimal qty;
}
