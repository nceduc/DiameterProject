package com.company.cassandraAPI.domain;

import com.datastax.driver.core.DataType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


import java.io.Serializable;
import java.math.BigDecimal;

@Table("balance")
public class Customer implements Serializable{

    @PrimaryKey
    @CassandraType(type = DataType.Name.VARCHAR)
    private String number;
    private BigDecimal balance;
    private String password;


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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "{" +
                "number='" + number + '\'' +
                ", balance='" + balance + '\'' +
                ", password='" + password + '\'' +
                "}";
    }



}
