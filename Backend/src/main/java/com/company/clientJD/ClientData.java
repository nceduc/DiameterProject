package com.company.clientJD;

import java.util.Date;

public class ClientData {

    private String balance;
    private Date date;
    private boolean isReceivedResp = true;


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

    public boolean isReceivedResp() {
        return isReceivedResp;
    }

    public void setReceivedResp(boolean received) {
        isReceivedResp = received;
    }
}
