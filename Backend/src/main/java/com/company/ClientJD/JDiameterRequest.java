package com.company.ClientJD;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdiameter.api.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class JDiameterRequest {


    private static final Logger logger = LogManager.getLogger(JDiameterRequest.class);
    private static JDiameterRequest INSTANCE = null;


    public ClientData getClientData(String clientID){
        ClientData clientData = null;
        Session session = null;
        Request request = null;


        session = getSession();
        if(session != null){
            request = formDiameterRequest(session, clientID);
            clientData = sendDiameterRequest(request, session);
        }

        return clientData;
    }


    private Session getSession(){
        Session session = null;

        try {
            session = JDiameterConnectServer.connectionSession.getNewSession();
        } catch (InternalException e) {
            logger.error("Connection with diameter server was failed or Diameter-Server failed. Need to reboot!\n" + e.getMessage());
        }

        return session;
    }


    private Request formDiameterRequest(Session session, String clientID){
        Request request = null;

        request = session.createRequest(3000, ApplicationId.createByAuthAppId(33333),"exchange.example.org","127.0.0.1"); //формируем запрос
        request.getAvps().addAvp(Avp.USER_IDENTITY, clientID.getBytes()); //положили клиентский ID в Avp.USER_IDENTITY

        return request;
    }


    private ClientData sendDiameterRequest(Request request, Session session){
        Avp avp = null;
        AvpSet avpSet = null;
        ClientData answer = null;
        Future<Message> response = null;

        try {
            response = session.send(request); //посылаем запрос
            avpSet = response.get().getAvps(); //получаем все AVP ответа
            avp = avpSet.getAvp(Avp.CHECK_BALANCE_RESULT);


            if(avp != null){
                //if AVP with balance is not empty
                answer = new ClientData();
                answer.setBalance(avp.getUTF8String());
                avp = avpSet.getAvp(Avp.TIME_STAMPS);
                answer.setDate(avp.getTime());

            }else{
                //if AVP with balance is empty
                avp = avpSet.getAvp(Avp.RESULT_CODE); //check errors

                if(avp != null){
                    if(avp.getInteger32() == ErrorCode.ERROR_TYPE_REQUEST){
                        logger.error("This client sends wrong type Diameter-requests.\n " + avpSet.getAvp(Avp.ERROR_MESSAGE).getUTF8String());
                    }

                    if(avp.getInteger32() == ErrorCode.RELOADING_APPS){
                        logger.warn("Some apps of server reloading... Wait!");
                    }

                    if(avp.getInteger32() == -3){
                        logger.warn("User does not signup in system");
                    }
                }
            }
        } catch (InternalException | IllegalDiameterStateException | RouteException | OverloadException e) {
            logger.error("Send diameter request failed\n" + e.getMessage());

        } catch (AvpDataException | InterruptedException | ExecutionException | NullPointerException e){
            logger.error("Balance was not received\n" + e.getMessage());

        }

        return answer;
    }


    public static JDiameterRequest getInstance(){
        if(INSTANCE == null){
            INSTANCE = new JDiameterRequest();
        }
        return INSTANCE;
    }


    private JDiameterRequest(){}

}
