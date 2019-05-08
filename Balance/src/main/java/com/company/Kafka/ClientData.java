package com.company.Kafka;

import java.io.Serializable;

import java.util.Date;

public class ClientData implements Serializable{

    private String balance;
    private Date date;
    private boolean isCassandraFail;

    public boolean isCassandraFail() {
        return isCassandraFail;
    }

    public void setCassandraFail(boolean cassandraFail) {
        isCassandraFail = cassandraFail;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
