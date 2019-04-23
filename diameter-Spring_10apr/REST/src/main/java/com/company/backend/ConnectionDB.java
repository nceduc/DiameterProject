package com.company.backend;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class ConnectionDB {

    private static final Logger logger = Logger.getLogger(ConnectionDB.class);

    public Connection connect(){
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Driver not found [ConnectionDB]\n" + e.getMessage());
        }

        try {
            Statement statement;

            if (connection != null) {
                statement = connection.createStatement();
                statement.execute("CREATE TABLE IF NOT EXISTS 'usertable' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'clientID' varchar, 'password' varchar)");
            }

        } catch (SQLException e) {
            logger.error("SQLite query 'CREATE TABLE' was failed [ConnectionDB]\n" + e.getSQLState());
        }

        logger.info("Successful connection with SQLite");
        return connection;
    }



}
