package com.interview.aquariux.trade.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name = "tb_wallet")
public class Wallet extends BaseVO {

    @CreatedDate
    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", updatable = false)
    Instant created;
    @NotNull
    String address;
    @Column(columnDefinition = "Decimal(19,5) default '50000'")
    private BigDecimal usdtBalance;
}
