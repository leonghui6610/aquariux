package com.interview.aquariux.trade.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@Table(name = "tb_symbol", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Symbol extends BaseVO {
    @CreatedDate
    Instant created;
    @Version
    @LastModifiedDate
    Instant modified;
    @NotNull
    private String name;
    private boolean allowedTrade;

    private BigDecimal bidPrice, bidQty, askPrice, askQty;
}
