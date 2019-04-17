package com.company.ServerJD;

import com.company.Kafka.KafkaRequest;
import org.jdiameter.api.*;
import org.jdiameter.common.impl.app.cca.JCreditControlAnswerImpl;
import org.jdiameter.common.impl.app.cca.JCreditControlRequestImpl;


public class Router implements NetworkReqListener {

    private KafkaRequest kafkaRequest = new KafkaRequest(); //для записей в кафку

    @Override
    public Answer processRequest(Request request) {
        System.out.println(request.getAvps());
        System.out.println("запрос пришел с BE");
        //этот метод вызывается при диаметровом запросе
        String clientID = null;

        //здесь мы должны разделять трафик

        //парсим запрос
        try {
            clientID = request.getAvps().getAvp(1).getUTF8String();
        } catch (AvpDataException e) {
            e.printStackTrace();
        }

        kafkaRequest.sendRecordKafka(clientID); //посылаем запись в кафку для получения баланса

        return null;
    }


}
