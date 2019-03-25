package com.company.backend;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.sql.*;

@RestController
@RequestMapping("login")
public class Authentication {

    private Connection connection;

    //надо добавить шифрование паролей либо вообще их убрать

    @PostMapping
    public String requestForAuthentication(HttpServletRequest req, HttpServletResponse resp) {
        String response = "Phone or password is invalid";
        String clientID = req.getParameter("clientID").trim();
        String password = req.getParameter("password").trim();

        if(valid(clientID, password)){
            if(clientID.charAt(0) == '+'){
                clientID = clientID.substring(1, clientID.length());
            }

            if(login(clientID, password)) {
                resp.addCookie(new Cookie("clientID", clientID));
                response = "<script>location.replace(\"/main\");</script>"; //редирект на главную страницу
            }
        }
        return response;
    }


    private boolean login(String clientID, String password){
        boolean isLogin = false;
        connection = new ConnectionDB().connect(); //соединяемся с БД
        Statement statement = null;
        ResultSet resultSet = null;
        String select = "SELECT clientID, password FROM usertable WHERE clientID = '" + clientID + "'";


        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(select);
        }catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Execute query failed");
        }

        //получаем запросы из БД
        try {
            if (resultSet.next()) {
                String clientIDFromDB = resultSet.getString("clientID");
                String passwordFromDB = resultSet.getString("password");
                if(clientIDFromDB.equals(clientID) && passwordFromDB.equals(password)){
                    isLogin = true; //авторизуем
                }
            } else{
                signup(clientID, password); //регистрируем
                isLogin = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if(statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isLogin;
    }


    private void signup(String clientID, String password){
        String insert = "INSERT INTO usertable(clientID, password) VALUES(?,?)";
        PreparedStatement prSt = null;

        try {
            prSt = connection.prepareStatement(insert);
            prSt.setString(1, clientID.toUpperCase());
            prSt.setString(2, password.toUpperCase());
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(prSt != null)
                    prSt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean valid(String clientID, String password){
        boolean isValid = false;
        if(clientID.trim().length() > 9 && password.length() > 3){
            isValid = true;
        }
        return isValid;
    }
}
