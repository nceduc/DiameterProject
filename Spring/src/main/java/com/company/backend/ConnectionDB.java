package com.company.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionDB {
    public Connection connect() {

        //  Database credentials
        String DB_URL = "jdbc:postgresql://127.0.0.1:5432/users";
        String USER = "postgres";
        String PASS = "postgres";



        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");
        Connection connection = null;

        try {
            connection =  DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
        }

        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println("Failed to make connection to database");
        }


        try {
            String query = "create table if not exists usertable (id serial, clientID varchar, password varchar)";
            Statement statement = null;
            if (connection != null) {
                statement = connection.createStatement();
                statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
