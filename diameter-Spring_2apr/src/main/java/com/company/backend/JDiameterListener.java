package com.company.backend;

import org.jdiameter.api.Answer;
import org.jdiameter.api.AvpDataException;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Каждый раз когда приходит ответ, мы парсим его и полуаем клиентский id и телефон.
 * Эти данные мы записываем в Map.
 * Когда посылаются запросы от пользователей, данная Map опрашивается на наличие баланса для клиентского id,
 * который мы достаем из запроса юзера.
 * Если значение баланса в Map существует, то получаем его и возвращаем.
 */


public class JDiameterListener implements EventListener<Request, Answer> {

    static Map<String, String> mapBalance = new HashMap<>();


    @Override
    public void receivedSuccessMessage(Request request, Answer answer) {
        //этот код сработает, когда придет ответ
        System.out.println("ответ пришел\n\n");

        try {
            String clientID = answer.getAvps().getAvp(2).getUTF8String();
            String balance = answer.getAvps().getAvp(3).getUTF8String();
            mapBalance.put(clientID, balance);
        } catch (AvpDataException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void timeoutExpired(Request request) {

    }
}
