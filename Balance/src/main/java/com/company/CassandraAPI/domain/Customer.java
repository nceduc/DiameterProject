package com.company.CassandraAPI.domain;

import com.datastax.driver.core.DataType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


import java.io.Serializable;
import java.math.BigDecimal;

@Table("customers")
public class Customer implements Serializable{

    @PrimaryKey
    @CassandraType(type = DataType.Name.VARCHAR)
    private String number;
    @Column
    private BigDecimal balance;


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "{" +
                "number='" + number + '\'' +
                ", balance='" + balance + '\'' +
                "}";
    }



}
