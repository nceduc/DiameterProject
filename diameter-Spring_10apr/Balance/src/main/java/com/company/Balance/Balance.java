package com.company.Balance;

public class Balance {
    static int i = 0;

    public String getBalance(String clientID){
        String balance;

        //query to Cassandra
        i++;
        return i+"";
    }

}
