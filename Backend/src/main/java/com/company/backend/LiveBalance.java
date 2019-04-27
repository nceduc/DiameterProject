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

    private JDiameterRequest request = new JDiameterRequest(); //создали экземпляр класса для отправки диаметровых реквестов ???

    @GetMapping
    public String balance(@PathVariable String clientID) {
        String balance = null;
        JSONObject jsonObject = null;

        balance = request.getBalance(clientID);

        if(balance == null){
            balance = "loading...";
        }
        //формируем ответ и возвращаем баланс
        jsonObject = new JSONObject();
        jsonObject.put("balance", balance);
        return jsonObject.toJSONString();
    }

}

