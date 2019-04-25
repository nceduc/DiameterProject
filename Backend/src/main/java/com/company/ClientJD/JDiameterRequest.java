package com.company.ClientJD;


import org.jdiameter.api.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;


public class JDiameterRequest {
    
    private static final Logger logger = Logger.getLogger(JDiameterRequest.class);

    public String getBalance(String clientID){
        Session session = getSession();
        Request request = formDiameterRequest(session, clientID);
        String balance = sendDiameterRequest(request, session);

        return balance;
    }

    private Session getSession(){
        Session session = null;

        try {
            session = JDiameterConnectServer.connectionSession.getNewSession();
        } catch (InternalException e) {
            logger.error("Connection with diameter server was failed [JDiameterRequest.class]\n" + e.getMessage());
        }
        return session;
    }


    private Request formDiameterRequest(Session session, String clientID){
        Request request = null;

        if(session != null){
            request = session.createRequest(3000, ApplicationId.createByAuthAppId(33333),"exchange.example.org","127.0.0.1"); //формируем запрос
            request.getAvps().addAvp(Avp.USER_IDENTITY, clientID.getBytes()); //положили клиентский ID в Avp.USER_IDENTITY
        }
        return request;
    }


    private String sendDiameterRequest(Request request, Session session){
        String balance = null;
        Future<Message> response;

        try {
            response = session.send(request);
            balance = response.get().getAvps().getAvp(Avp.CHECK_BALANCE_RESULT).getUTF8String(); //получаем баланс

        } catch (ExecutionException | IllegalDiameterStateException | InterruptedException | OverloadException | AvpDataException | RouteException | InternalException e) {
            logger.error("Send diameter request was failed [JDiameterRequest.class]\n" + e.getMessage());
        } catch (Exception e) {
            logger.error(e.getStackTrace());
        }


        return balance;
    }


}
