package com.company.backend;
import org.jdiameter.api.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import static com.company.backend.JDiameterClient.clientSessionFactory;

/**
 * В данном классе каждый новый запрос к getBalance() создает новый поток,
 * затем запрос отправляется вызовом sendRequest() в этом же потоке.
 * Когда приходит ответ от сервера, вызывается receivedSuccessMessage() и создается второй поток,
 * работающий только внутри данного метода.
 * Поэтому мы останавливаем основной (первый) поток до тех пор, пока не придет ответ с балансом
 * во втором потоке.
 */

@RestController
@RequestMapping("balance")
public class LiveBalance {

    private final Object syncObj = new Object(); //for wait-notify
    private String balance;


    @PostMapping
    public String getBalance(@RequestBody String messageJSON) {
        //перевод запроса в диаметровый
        String clientID = parseJSON(messageJSON);

        sendRequest(clientID); //посылаем диаметровый запрос и получаем баланс
        System.out.println(balance);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("balance", balance);
        return jsonObject.toJSONString();
    }




    private void sendRequest(String clientID){
        Session session = null;

        try {
            session = clientSessionFactory.getNewSession();
        } catch (InternalException e) {
            e.printStackTrace();
        }
        Request request = session.createRequest(7, ApplicationId.createByAuthAppId(33333),"exchange.example.org","127.0.0.1"); //формируем запрос
        request.getAvps().addAvp(1, clientID.getBytes()); //положили номер в AVP в запрос


        try {
            session.send(request, new GetAnswer()); //посылаем диаметровый запрос
        } catch (Exception e){
            e.printStackTrace();
        }


        //останавливаем поток, пока не придет ответ от сервера с балансом
        try{
            synchronized(syncObj) {
                syncObj.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private String parseJSON(String json){
        String clientID = null;
        try {
            JSONObject objectJson = (JSONObject) new JSONParser().parse(json);
            clientID = objectJson.get("clientID").toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return clientID;
    }


    //внутренний класс для парсинга ответа на запрос
    class GetAnswer implements EventListener<Request, Answer>{
        @Override
        public void receivedSuccessMessage(Request request, Answer answer) {
            //этот код сработает, когда придет ответ
            System.out.println("Answer received");

            try {
                balance = answer.getAvps().getAvp(2).getUTF8String();
            } catch (AvpDataException e) {
                e.printStackTrace();
            }

            //ответ пришел, возобновляем работу потока
            synchronized(syncObj){
                syncObj.notify();
            }
        }
        @Override
        public void timeoutExpired(Request request) {

        }
    }


}

