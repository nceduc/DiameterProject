package com.company.backend.controller;

import com.company.clientJD.ClientData;
import com.company.clientJD.JDiameterRequest;
import org.json.simple.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;


@RestController
public class BalanceController {


    @GetMapping("/dashboard/getbal")
    public String balance() {

        String clientID = SecurityContextHolder.getContext().getAuthentication().getName();
        JDiameterRequest request = null;
        String balance = null;
        String time = null;
        ClientData clientData = null;
        JSONObject jsonObject = null;

        request = JDiameterRequest.getInstance(); //получили экземпляр
        clientData = request.getClientData(clientID);
        balance = clientData.getBalance();
        time = String.valueOf(clientData.getDate().getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar ansTime = Calendar.getInstance();
        Long timeLong = new Long(time);
        ansTime.setTimeInMillis(timeLong);

        if(clientData == null){
            balance = "Currently disabled because our server have some problem. Please, try again later.";
        }
        //формируем ответ и возвращаем данные
        jsonObject = new JSONObject();
        jsonObject.put("balance", balance);
        jsonObject.put("time", dateFormat.format(ansTime.getTime()));
        return jsonObject.toJSONString();
    }


}

