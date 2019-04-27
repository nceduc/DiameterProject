package com.company.ClientJD;


import org.jdiameter.api.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;


public class JDiameterRequest {
    
    private static final Logger logger = Logger.getLogger(JDiameterRequest.class);

    //флаги для идентификации неработающих приложений
    private static boolean isDiameterServerFail = false;
    private static boolean isKafkaFail = false;
    private static boolean isCassandraFail = false;


    public String getBalance(String clientID){
        Session session = null;
        Request request = null;
        String balance = null;

        if (!isDiameterServerFail && !isKafkaFail && !isCassandraFail){
            session = getSession();

            if(session == null){
                isDiameterServerFail = true;
            }else{
                request = formDiameterRequest(session, clientID);
                balance = sendDiameterRequest(request, session);
            }

        }

        return balance;
    }

    private Session getSession(){
        Session session = null;

        try {
            session = JDiameterConnectServer.connectionSession.getNewSession();
        } catch (InternalException e) {
            logger.error("Connection with diameter server was failed or Diameter-Server failed. Need to reboot! [JDiameterConnectServer.class]\n" + e.getMessage());
        }
        return session;
    }

    private Request formDiameterRequest(Session session, String clientID){
        Request request = null;

        request = session.createRequest(3000, ApplicationId.createByAuthAppId(33333),"exchange.example.org","127.0.0.1"); //формируем запрос
        request.getAvps().addAvp(Avp.USER_IDENTITY, clientID.getBytes()); //положили клиентский ID в Avp.USER_IDENTITY

        return request;
    }

    private String sendDiameterRequest(Request request, Session session){
        Avp avp = null;
        AvpSet avpSet = null;
        String answer = null;
        Future<Message> response = null;

        try {
            response = session.send(request); //послыаем запрос
            avpSet = response.get().getAvps(); //получаем все AVP ответа

            avp = avpSet.getAvp(Avp.CHECK_BALANCE_RESULT);

            if(avp != null){
                //if AVP with balance is not empty
                answer = avp.getUTF8String(); //получаем баланс

            }else{
                //if AVP with balance is empty
                avp = avpSet.getAvp(Avp.RESULT_CODE); //check errors

                if(avp != null){
                    if(avp.getInteger32() == ErrorCode.KAFKA_FAILED){
                        logger.error("Kafka failed [Backend]");
                        isKafkaFail = true;
                    }

                    if(avp.getInteger32() == ErrorCode.ERROR_TYPE_REQUEST){
                        logger.error("This client sends wrong type Diameter-requests.\n " + avpSet.getAvp(Avp.ERROR_MESSAGE).getUTF8String());
                    }

                    if(avp.getInteger32() == ErrorCode.CASSANDRA_FAILED){
                        logger.error("App 'Balance' or Cassandra failed [Backend]");
                        isCassandraFail = true;
                    }

                }
            }

        } catch (InternalException | IllegalDiameterStateException | RouteException | OverloadException e) {
            logger.error("Send diameter request failed [JDiameterRequest.class]\n" + e.getMessage());

        } catch (AvpDataException | InterruptedException | ExecutionException | NullPointerException e){
            logger.error("Balance was not received [JDiameterRequest.class]\n" + e.getMessage());

        }

        return answer;
    }


}
