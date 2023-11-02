package com.interview.aquariux.trade.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name = "tb_crypto_holdings", uniqueConstraints = @UniqueConstraint(columnNames = {"walletAddress", "symbol", "usdtPerUnit"}))
public class CryptoHoldings extends BaseVO {
    @CreatedDate
    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", updatable = false)
    Instant created;
    @Version
    @LastModifiedDate
    Instant modified;
    @NotNull
    private String symbol;
    @NotNull
    private BigDecimal qty;
    @NotNull
    private BigDecimal usdtPerUnit;
    @NotNull
    private boolean inWallet;
    @NotNull
    private String walletAddress;
    @NotNull
    private Long walletId;
}
