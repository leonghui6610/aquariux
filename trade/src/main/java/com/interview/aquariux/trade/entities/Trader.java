package com.interview.aquariux.trade.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Data
@Entity
@Table(name = "tb_trader", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class Trader extends BaseVO {

    @CreatedDate
    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", updatable = false)
    Instant created;
    @Column(length = 50)
    private String name;
    @NotNull
    @Email
    @Size(max = 100)
    private String email;
    @NotNull
    private String walletAdd;
    @NotNull
    private Long walletId;
}
