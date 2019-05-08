package com.company.Balance;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class Balance{

    private static Logger logger = LogManager.getLogger(Balance.class);
    public static Session connection = new CassandraConnector().connect(); //connection with Cassandra

    public String getBalance(String clientID){
        String balance = null;
        try {
            balance = selectDB(clientID);
        }catch (NullPointerException ex){
            logger.error("There isn't balance for the clientID");
        }
        return balance;
    }


    private String selectDB(String clientID){
        BigDecimal answer = null;
        final String query = "SELECT * from company.balance WHERE number = ?";
        ResultSet resultSet = null;
        Row row = null;
        try {
            resultSet = connection.execute(query, clientID);
            row = resultSet.one();
            answer = row.getDecimal("balance");
        }
        catch (InvalidQueryException e){
            logger.error("Error select balance from Cassandra" + e.getMessage());
        }
        return String.valueOf(answer);
    }


    public boolean isCassandraRunning(){
        boolean result = false;
        final String query = "SELECT now() FROM system.local"; //test query

        try {
            if(connection == null){
                connection = new CassandraConnector().connect();
                logger.warn("Try to connect...");
            }
            if(connection != null){
                connection.execute(query);
                result = true;
            }
        }
        catch (NoHostAvailableException ex){
            logger.error("Cassandra failed " + ex.getMessage());
        }
        return result;
    }





    //for testing
    {
        createTable();
        for(int i = 0; i<30; i++){
            this.update("79005091262"+i, 1000+i);
        }
    }


    private void createTable(){
        String query = "CREATE KEYSPACE IF NOT EXISTS company WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '2'};";
        connection.execute(query);
        System.out.println("Keyspace company created");
        query = "CREATE TABLE IF NOT EXISTS company.balance (number varchar, balance double, PRIMARY KEY(number));";
        connection.execute(query);
        System.out.println("Table balance created");
    }


    private void update(String number, double balance) {
        final String query = "INSERT INTO company.balance (number,balance) VALUES (?, ?)";
        connection.execute(query, number, new BigDecimal(balance));
        System.out.println("------------Update успешен---------------");
    }

    //dor testing


}





