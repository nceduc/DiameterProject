package com.company.backend;

import com.company.clientJD.ClientData;
import com.company.clientJD.JDiameterRequest;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
@RequestMapping(value = "balance/{clientID}", method = GET)
public class LiveBalance {


    @GetMapping
    public String balance(@PathVariable String clientID) {
        JDiameterRequest request = null;
        String balance = null;
        String time = null;
        ClientData clientData = null;
        JSONObject jsonObject = null;

        request = JDiameterRequest.getInstance(); //получили экземпляр
        clientData = request.getClientData(clientID);

        if(clientData != null){
            balance = clientData.getBalance();
            time = String.valueOf(clientData.getDate().getTime());
            System.out.println(balance);
            System.out.println(time);
        }else{
            balance = "loading...";
        }


        //формируем ответ и возвращаем данные
        jsonObject = new JSONObject();
        jsonObject.put("balance", balance);
        return jsonObject.toJSONString();
    }

}

