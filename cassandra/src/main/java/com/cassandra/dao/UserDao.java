package com.cassandra.dao;

import com.cassandra.models.User;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.cassandra.utils.CassandraConnector;
import com.datastax.driver.core.exceptions.InvalidQueryException;

public class UserDao {
   private static final CassandraConnector client = new CassandraConnector();


   public Double findByNumber(String id) throws NullPointerException {
        Double ans = null;
        String query = "SELECT * from ncproject.balance WHERE number = ?";
        try {ResultSet queryResult=client.getSession().execute(query,id);
        Row ansRow = queryResult.one();
        ans = ansRow.getDouble("balance");}
        catch (InvalidQueryException e){
            System.out.println("Ошибка в findByNumber. " + e.getMessage());
        }
        return ans;
   }


    public void update(User user) {
        String query = "INSERT INTO ncproject.balance (number,balance) VALUES (?, ?)";
        try {client.getSession().execute(query, user.getNumber(),user.getBalance());
            System.out.println("------------Update успешен---------------");}
        catch (InvalidQueryException e){
            final String createKeyspaceCql =
                    "CREATE KEYSPACE IF NOT EXISTS ncproject WITH replication = {\n" +
                            "  'class': 'SimpleStrategy',\n" +
                            "  'replication_factor': '2'\n" +
                            "};";
            client.getSession().execute(createKeyspaceCql);
            System.out.println("Keyspace ncproject created");
            final String createTableCql =
                    "CREATE TABLE if not exists ncproject.balance (number varchar, balance double, PRIMARY KEY(number))";
            client.getSession().execute(createTableCql);
            System.out.println("Table balance created");
            try {
                client.getSession().execute(query, user.getNumber(),user.getBalance());
            System.out.println("------------Update успешен---------------");}
            catch (InvalidQueryException e1){
                e.printStackTrace();
            }
        }
    }

    public void delete(User user) throws NullPointerException {
        String query = "DELETE FROM ncproject.balance WHERE number = ?";
        try {client.getSession().execute(query,user.getNumber());
            System.out.println("------------Delete успешен---------------");}
        catch (InvalidQueryException e) {
            System.out.println("Ошибка в Delete. " + e.getMessage());
        }
    }
    public static void closeConnection(){
        client.close();
        System.out.println("Подключение к БД успешно закрыто.");
    }
}