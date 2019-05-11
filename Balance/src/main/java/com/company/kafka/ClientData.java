package com.company.kafka;

import java.io.Serializable;

import java.util.Date;

public class ClientData implements Serializable{

    private String balance;
    private Date date;
    private boolean isClientNotFound;

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

    public boolean isClientNotFound() {
        return isClientNotFound;
    }

    public void setClientNotFound(boolean clientNotFound) {
        isClientNotFound = clientNotFound;
    }
}
