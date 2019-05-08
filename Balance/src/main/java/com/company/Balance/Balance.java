package com.company.Balance;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class Balance{

    private static Logger logger = LogManager.getLogger(Balance.class);
    private Session session = new CassandraConnector().connect(); //соединяемся с Cassandra

    public String getBalance(String clientID){
        String balance = null;
        try {
            balance = selectBalanceDB(clientID);
        }catch (NullPointerException ex){
            logger.error("There isn't balance for the clientID");
        }
        return balance;
    }




    private String selectBalanceDB(String clientID){
        System.out.println(clientID);
        BigDecimal balance = null;
        String query = "SELECT balance from company.balance WHERE number = ?";
        try {ResultSet queryResult = session.execute(query,clientID);
            Row ansRow = queryResult.one();
            balance = ansRow.getDecimal("balance");
        }
        catch (InvalidQueryException e){
            logger.error("Error select balance from Cassandra" + e.getMessage());
        }
        return String.valueOf(balance);
    }



    //for testing
    {
        for(int i = 0; i<30; i++){
            this.update("79005091262"+i, 21.2+i);
        }
    }
    private void update(String number, double balance) {
        BigDecimal getBal = new BigDecimal(balance);
        String query = "INSERT INTO company.balance (number,balance) VALUES (?, ?)";
            try {
                session.execute(query, number, getBal);
                System.out.println("------------Update успешен---------------");}
            catch (InvalidQueryException e){
                e.printStackTrace();
            }
        }
    }

