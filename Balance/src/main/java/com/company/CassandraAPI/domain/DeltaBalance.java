package com.company.CassandraAPI.domain;

import java.math.BigDecimal;

public class DeltaBalance {

    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "{" +
                "balance='" + balance + '\'' +
                "}";
    }
}
