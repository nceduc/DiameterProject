package com.company.backend.controller;

import com.company.clientJD.ClientData;
import com.company.clientJD.JDiameterRequest;
import org.json.simple.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@RestController
public class BalanceController {


    @GetMapping("/dashboard/getbal")
    public String balance() {

        String clientID = SecurityContextHolder.getContext().getAuthentication().getName();
        JDiameterRequest request = null;
        String response = null;
        long timeLong = 0L;
        ClientData clientData = null;
        JSONObject jsonObject = null;
        SimpleDateFormat dateFormat = null;
        String date = null;

        request = JDiameterRequest.getInstance(); //получили экземпляр
        clientData = request.getClientData(clientID); //получили данные

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(clientData != null){
            response = clientData.getBalance();
            timeLong = (clientData.getDate().getTime() + (1000*60*60*3)); //переводим время на Россию
            date = dateFormat.format(timeLong);
        }else{
            response = "Currently disabled because our server have some problem. Please, try again later";
            date = dateFormat.format(new Date().getTime());
        }

        //формируем ответ и возвращаем данные
        jsonObject = new JSONObject();
        jsonObject.put("response", response);
        jsonObject.put("date", date);
        return jsonObject.toJSONString();
    }


}

