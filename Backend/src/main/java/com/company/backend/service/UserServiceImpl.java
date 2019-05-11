package com.company.backend.service;

import com.company.backend.entity.User;
import net.minidev.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder getBCryptPasswordEncoder;

    private final String USER_AGENT = "Mozilla/5.0";

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);


    @Override
    public User getUser(String login) {
        User user = new User(null, null);
        String password = null;

        try {
            password = sendGet(login);
            if (password == null) return null;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
        user.setLogin(login);
        user.setPassword(password);
        return user;
    }

    @Override
    public void save(User user) throws ConnectException {
        user.setPassword(getBCryptPasswordEncoder.encode(user.getPassword()));
        JSONObject userJson = toJson(user);
        try {
            sendPost(userJson);
        } catch (ConnectException e) {
            throw new ConnectException("Backend disabled");
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    public JSONObject toJson(User user) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("number", user.getLogin());
        jsonObject.put("password", user.getPassword());
        jsonObject.put("balance", 0);
        System.out.println(jsonObject.toJSONString());
        return jsonObject;
    }

    private void sendPost(JSONObject userJson) throws Exception {

        try {

            String url = "http://localhost:8090/customers";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            // Send post request
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(userJson.toJSONString().getBytes());

            int responseCode = con.getResponseCode();
            if (responseCode!=201){
                throw new ConnectException("Backend disabled");
            }
            String responseMessage = con.getResponseMessage();


            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            System.out.println("Response Message: " + responseMessage); //проверить, что вернет сервер

            wr.flush();
            wr.close();
        }
        catch (ConnectException e){
            logger.error("Can't post to backend. Please, check status.");
            throw new ConnectException("Backend disabled");
        }
    }


    private String sendGet(String login) throws Exception {

        String url = "http://localhost:8090/customers/auth/" + login;

        URL obj = new URL(url);
        try {
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());

            return response.toString();
        }
        catch (ConnectException e) {
            logger.error("Can't get to backend. Please, check status.");
            return null;
        }
        catch (IOException e) {
            return null;
        }
    }
}
