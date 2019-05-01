package com.company.Balance;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Balance{

    private static Logger logger = LogManager.getLogger(Balance.class);
    private Session session = new CassandraConnector().connect(); //соединяемся с Cassandra

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
        Double answer = null;
        final String query = "SELECT * from company.balance WHERE number = ?";
        ResultSet resultSet = null;
        Row row = null;
        try {
            resultSet = session.execute(query, clientID);
            row = resultSet.one();
            answer = row.getDouble("balance");
        }
        catch (InvalidQueryException e){
            logger.error("Error select balance from Cassandra" + e.getMessage());
        }
        return String.valueOf(answer);
    }



    //for testing
    {
        for(int i = 0; i<30; i++){
            this.update("79005091262"+i, 21.2+i);
        }
    }
    private void update(String number, double balance) {
        String query = "INSERT INTO company.balance (number,balance) VALUES (?, ?)";
        try {
            session.execute(query, number, balance);
            System.out.println("------------Update успешен---------------");}
        catch (InvalidQueryException e){
            final String createKeyspaceCql =
                    "CREATE KEYSPACE IF NOT EXISTS company WITH replication = {\n" +
                            "  'class': 'SimpleStrategy',\n" +
                            "  'replication_factor': '2'\n" +
                            "};";
            session.execute(createKeyspaceCql);
            System.out.println("Keyspace company created");
            final String createTableCql =
                    "CREATE TABLE if not exists company.balance (number varchar, balance double, PRIMARY KEY(number))";
            session.execute(createTableCql);
            System.out.println("Table balance created");
            try {
                session.execute(query, number, balance);
                System.out.println("------------Update успешен---------------");}
            catch (InvalidQueryException e1){
                e.printStackTrace();
            }
        }
    }

}
