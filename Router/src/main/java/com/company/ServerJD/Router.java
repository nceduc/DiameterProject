package com.company.ServerJD;

import com.company.Kafka.ClientData;
import com.company.Kafka.KafkaProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdiameter.api.*;
import static com.company.Kafka.KafkaRequest.*;
import static com.company.failApps.FailApps.*;

public class Router implements NetworkReqListener {

    private static final Logger logger = LogManager.getLogger(Router.class);

    static {
        //init dictionary
        InfoDiameterRequest.initDictionary();
        System.out.println("Dictionary loaded");
        logger.info("Dictionary loaded");
    }

    @Override
    public Answer processRequest(Request request) {
        System.out.println("\nReceived request from BE");
        //этот метод вызывается при диаметровом запросе
        AvpSet avpSet = null;
        long codeDiameterAppId = 0L;
        int codeDiameterRequest = 0;
        String appName = null;
        String requestName = null;
        ClientData clientData = null;
        String clientID = null;
        String balance = null;
        Answer answer = null;
        String message = null;


        codeDiameterAppId = request.getApplicationId(); //получаем код диаметрового приложения
        codeDiameterRequest = request.getCommandCode(); //получаем код диаметрового запроса

        if(codeDiameterRequest == CodeRequest.GET_BALANCE_REQUEST){
            //this is Get-Balance-Request
            avpSet = request.getAvps(); //получаем все Avp реквеста
            answer = request.createAnswer(); //начинаем формировать ответ для BE


            try {
                clientID = avpSet.getAvp(Avp.USER_IDENTITY).getUTF8String(); //получаем clientID для запроса баланса
                System.out.println(clientID);
            } catch (AvpDataException e) {
                logger.error("AVP is invalid\n" + e.getMessage());
            } catch (NullPointerException e) {
                logger.error("ClientID was not received\n" + e.getMessage());
            }



            if(appsRunning()){
                if(isKafkaRunning()){
                    while(true){
                        if(runningCassandra(clientID)){ //доп. проверка на то, чтобы запросы не отправлялись в Кассандру, если она fail
                            writeRecordKafka(clientID);
                        }
                        clientData = KafkaProcessor.mapData.get(clientID);
                        //пишем запись в кафку до тех пор, пока не придет какое либо val
                        if(clientData != null) {
                            System.out.print("\n\n"+clientData.isCassandraFail()+"\n\n");
                            if(!isReloading && clientData.isCassandraFail()){
                                logger.error("Cassandra failed. Restart...");
                                isCassandraFail = true;
                                clientData.setCassandraFail(false);
                                KafkaProcessor.mapData.put(clientID, clientData);
                                reloadApps();
                                break;
                            }else {
                                clientData.setCassandraFail(false);
                                balance = clientData.getBalance();
                                answer.getAvps().addAvp(Avp.CHECK_BALANCE_RESULT, balance.getBytes()); //положили баланс в ответ
                                break;
                            }
                        }
                    }
                }else {
                    //если кафка не работает
                    logger.error("Kafka failed. Restart...");
                    isKafkaFail = true;
                    reloadApps();
                }
            }else{
                //если приложения в стадии перезагрузки
                message = "Some apps of server reloading";
                answer.getAvps().addAvp(Avp.RESULT_CODE, -2);
                answer.getAvps().addAvp(Avp.ERROR_MESSAGE, message.getBytes()); //возвращаем диаметровый ответ с сообщением об ошибке
                logger.warn("Some apps of server reloading... Wait!");
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
