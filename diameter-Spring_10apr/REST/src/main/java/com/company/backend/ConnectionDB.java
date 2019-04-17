package com.company.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class ConnectionDB {
    static Statement statement;
    Connection connect(){
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            statement = connection.createStatement();

            if(connection != null){
                statement.execute("CREATE TABLE IF NOT EXISTS 'usertable' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'clientID' varchar, 'password' varchar)");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

}