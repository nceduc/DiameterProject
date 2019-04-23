package com.company.backend;

import com.company.ClientJD.JDiameterRequest;
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

        String balance = new JDiameterRequest().getBalance(clientID);

        //формируем ответ и возвращаем баланс
        JSONObject jsonObject = new JSONObject();
        if(balance == null){
            balance = "loading...";
        }
        jsonObject.put("balance", balance);
        return jsonObject.toJSONString();
    }

}

