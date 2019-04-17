package com.cassandra.models;

public class User {
    private String number;
    private double balance;

    public User(){
    }

    public User(String number, double balance){
    this.number =number;
    this.balance=balance;
    }

    public String getNumber() {return number;}

    public void setNumber(String name) {this.number = name;}

    public double getBalance() {return balance;}

    public void setBalance(double balance) {this.balance=balance;}

}
