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
        String response = null;
        try {
            String clientID = req.getParameter("clientID").trim();
            String password = req.getParameter("password").trim();
            if(clientID.trim().length() < 11 || password.length() < 4){
                throw new Exception();
            }

            if(clientID.charAt(0) == '+'){
                clientID = clientID.substring(1, clientID.length());
            }



            //проверяем, есть ли такой номер в БД, если есть сравниваем пароль, и если все ок, то редирект
            //если такого номера нет, то создаем новую запись в БД (номер телефона, пароль) и редирект на главную
            if(login(clientID, password)) {
                resp.addCookie(new Cookie("clientID", clientID));
                response = "<script>location.replace(\"/main\");</script>"; //редирект на главную страницу
            }else{
                response = "Phone or password is invalid";
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e){
            response = "Phone or password is invalid";
        }

        return response;
    }


    private boolean login(String clientID, String password){
        boolean isLogin = false;
        connection = new ConnectionDB().connect(); //соединяемся с БД
        Statement statement;
        ResultSet resultSet = null;
        String select = "SELECT id, clientID, password FROM usertable WHERE clientID = '" + clientID + "'";


        try{
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(select);
        }catch (SQLException ex) {
            System.out.println("Execute query failed");
        }

        //получаем запросы из БД
        try {
            assert resultSet != null;
            if (!resultSet.next()) { //проверили на пустоту
                throw new SQLException();
            }
            resultSet.beforeFirst(); //вернули указатель на начало
            while (resultSet.next()) {
                String clientIDFromDB = resultSet.getString("clientID");
                String passwordFromDB = resultSet.getString("password");

                if(clientIDFromDB.equals(clientID) && passwordFromDB.equals(password)){
                    isLogin = true;
                }
            }

        } catch (SQLException e) {
            signup(clientID, password);
            isLogin = true;
        }finally {
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
        }finally {
            try {
                if(connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
