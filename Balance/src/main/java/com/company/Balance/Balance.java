package com.company.Balance;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import org.apache.log4j.Logger;

public class Balance{


    private static final Logger logger = Logger.getLogger(Balance.class);
    private Session session = new CassandraConnector().connect(); //соединяемся с Cassandra

    public String getBalance(String clientID){
        return selectBalanceDB(clientID);
    }

    {
        for(int i = 0; i<30; i++){
            this.update("79005091262"+i, 21.2+i);
        }


    }

    private String selectBalanceDB(String clientID){
        System.out.println(clientID);
        Double balance = null;
        String query = "SELECT * from company.balance WHERE number = ?";
        try {ResultSet queryResult = session.execute(query,clientID);
            Row ansRow = queryResult.one();
            balance = ansRow.getDouble("balance");
        }
        catch (InvalidQueryException e){
            logger.error("Ошибка в getBalance [Balance.class] " + e.getMessage());
        }

        return String.valueOf(balance);
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
