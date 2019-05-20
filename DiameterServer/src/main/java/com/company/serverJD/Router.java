package com.company.serverJD;

import com.company.failApps.CheckFailApps;
import com.company.failApps.FailApps;
import com.company.kafka.ClientData;
import com.company.kafka.KafkaRequest;
import com.company.kafka.ProcessKafkaListener;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdiameter.api.*;
import org.mobicents.diameter.dictionary.AvpDictionary;

import java.util.Date;

public class Router implements NetworkReqListener {


    private static final Logger logger = LogManager.getLogger(Router.class);


    {
        //init dictionary
        try {
            AvpDictionary.INSTANCE.parseDictionary(this.getClass().getClassLoader().getResourceAsStream("dictionary.xml"));
            InfoDiameterRequest.initDictionary();
            System.out.println("Dictionary loaded");
            logger.info("Dictionary loaded");
            KafkaRequest.setProperties();
            KafkaRequest.writeRecord("testRecord");
        } catch (Exception e) {
            logger.error("Dictionary loading failed");
        }
    }

    @Override
    public Answer processRequest(Request request) {
        System.out.println("\nReceived request from BE");
        //этот метод вызывается при диаметровом запросе
        AvpSet avpSet = null;
        long codeDiameterAppId = 0L;
        int codeDiameterRequest = 0;
        int countKafkaRequest = 0;
        final int LIMIT_REQUEST = 600;
        String appName = null;
        String requestName = null;
        ClientData clientData = null;
        String clientID = null;
        String balance = null;
        boolean isClientNotFound = false;
        boolean isSecondRequest = false;
        Date date = null;
        Answer answer = null;
        String message = null;
        FailApps failApps = null;

        codeDiameterAppId = request.getApplicationId(); //получаем код диаметрового приложения
        codeDiameterRequest = request.getCommandCode(); //получаем код диаметрового запроса

        if(codeDiameterRequest == CodeRequest.GET_BALANCE_REQUEST){
            //this is Get-Balance-Request
            avpSet = request.getAvps(); //получаем все Avp реквеста
            answer = request.createAnswer(); //начинаем формировать ответ для BE
            failApps = FailApps.getInstance();



            try {
                clientID = avpSet.getAvp(Avp.USER_IDENTITY).getUTF8String(); //получаем clientID для запроса баланса
                System.out.println(clientID);
            } catch (AvpDataException e) {
                logger.error("AVP is invalid\n" + e.getMessage());
            } catch (NullPointerException e) {
                logger.error("ClientID was not received\n" + e.getMessage());
            }


            clientData = ProcessKafkaListener.mapData.get(clientID);
            if (failApps.appsRunning()){
                if (clientData != null) {
                    isClientNotFound = clientData.isClientNotFound();
                }
                if (!isClientNotFound && CheckFailApps.isKafkaRunning()) {
                    KafkaRequest.writeRecord(clientID); //не пишем в кафку, если юзер не зарегестрирован в системе
                }

                while(CheckFailApps.isKafkaRunning()) {
                    System.out.println("Запрос номер: " + countKafkaRequest);
                    clientData = ProcessKafkaListener.mapData.get(clientID);
                    if (clientData != null) {
                        break;
                    }else if(!isSecondRequest){
                        KafkaRequest.writeRecord(clientID);
                        isSecondRequest = true;
                    }
                    if(countKafkaRequest > LIMIT_REQUEST){
                        logger.warn("Limit requests expired. App 'Balance' may be failed.");
                        break;
                    }
                    countKafkaRequest++;
                }
            }

            //формируем соотвествующий диаметровый ответ
            if(clientData != null){
                balance = clientData.getBalance();
                date = clientData.getDate();
                if(!clientData.isClientNotFound()){
                    answer.getAvps().addAvp(Avp.CHECK_BALANCE_RESULT, balance.getBytes()); //положили баланс в ответ
                    answer.getAvps().addAvp(Avp.TIME_STAMPS, date); //положили дату в ответ
                }else{
                    message = "User does not signup in system";
                    answer.getAvps().addAvp(Avp.RESULT_CODE, -3);
                    answer.getAvps().addAvp(Avp.ERROR_MESSAGE, message.getBytes()); //возвращаем диаметровый ответ с сообщением об ошибке
                    logger.warn("User does not signup in system");
                }
            }else if(!failApps.appsRunning()){
                //если какое-то приложение не работает
                message = "Some apps of server don't work";
                answer.getAvps().addAvp(Avp.RESULT_CODE, -2);
                answer.getAvps().addAvp(Avp.ERROR_MESSAGE, message.getBytes()); //возвращаем диаметровый ответ с сообщением об ошибке
                logger.warn("Some apps of server don't work");
            }else{
                answer.getAvps().addAvp(Avp.CHECK_BALANCE_RESULT, balance.getBytes()); //положили баланс в ответ
                answer.getAvps().addAvp(Avp.TIME_STAMPS, date); //положили дату в ответ
            }




        }else{
            //this is undefined request
            appName = InfoDiameterRequest.getAppName(codeDiameterAppId);
            requestName = InfoDiameterRequest.getRequestName(codeDiameterRequest);

            //формируем ответ с информацией об ошибке
            answer = request.createAnswer();

            message = "This type request ["+requestName+"] is not supported by the server";
            answer.getAvps().addAvp(Avp.RESULT_CODE, -1);
            answer.getAvps().addAvp(Avp.ERROR_MESSAGE, message.getBytes()); //возвращаем диаметровый ответ с сообщением об ошибке
            logger.warn("Such type request is not supported by this diameter-server [ AppName:"+appName+" RequestName:"+requestName+" ]");
        }

        return answer;
    }

}
