package com.company.ServerJD;

import org.jdiameter.api.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Каждый раз когда приходит ответ, мы парсим его и полуаем клиентский id и телефон.
 * Эти данные мы записываем в Map.
 * Когда посылаются запросы от пользователей, данная Map опрашивается на наличие баланса для клиентского id,
 * который мы достаем из запроса юзера.
 * Если значение баланса в Map существует, то получаем его и возвращаем.
 */


public class JDiameterListener implements NetworkReqListener {

    public static Map<String, String> mapBalance = new HashMap<>();


    @Override
    public Answer processRequest(Request request) {

        //парсим реквест для получения баланса и телефона
        try {
            String clientID = request.getAvps().getAvp(2).getUTF8String();
            String balance = request.getAvps().getAvp(3).getUTF8String();
            mapBalance.put(clientID, balance);
        } catch (AvpDataException e) {
            e.printStackTrace();
        }
        return null;
    }
}
