package com.company.clientJD;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdiameter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class JDiameterRequest {


    private static final Logger logger = LogManager.getLogger(JDiameterRequest.class);
    private static JDiameterRequest INSTANCE = null;
    private static Map<String, ClientData> dataReq = new HashMap<>();


    public static JDiameterRequest getInstance(){
        if(INSTANCE == null){
            INSTANCE = new JDiameterRequest();
        }
        return INSTANCE;
    }


    public ClientData getClientData(String clientID){
        ClientData clientData = null;
        Session connection = null;
        Request request = null;


        connection = getConnection();
        if(connection != null){
            request = formDiameterRequest(connection, clientID);
            clientData = sendDiameterRequest(request, connection, clientID); //получили новые данные
        }else{
            logger.error("JDiameter server failed. Need to reload!");
        }


        if(clientData == null){
            if(dataReq.get(clientID) != null){
                clientData = dataReq.get(clientID); //берем старые данные
            }
        }

        return clientData;
    }


    private Session getConnection(){
        Session session = null;
        try {
            session = JDiameterConnectServer.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }


    private Request formDiameterRequest(Session session, String clientID){
        Request request = null;

        request = session.createRequest(3000, ApplicationId.createByAuthAppId(33333),"exchange.example.org","127.0.0.1"); //формируем запрос
        request.getAvps().addAvp(Avp.USER_IDENTITY, clientID.getBytes()); //положили клиентский ID в Avp.USER_IDENTITY

        return request;
    }


    private ClientData sendDiameterRequest(Request request, Session session, String clientID){
        ClientData clientData = null;
        Future<Message> response = null;

        try {
            clientData = dataReq.get(clientID);
            if(clientData != null){
                if(clientData.isReceivedResp()){
                    clientData.setReceivedResp(false);
                    response = session.send(request); //посылаем запрос
                }
            }else{
                //this is the first request
                response = session.send(request); //посылаем запрос
            }


            if(response != null){
                //Diameter response received
                clientData = parseDiameterResponse(response); //получаем новые данные
                if(clientData != null){
                    clientData.setReceivedResp(true);
                    dataReq.put(clientID, clientData);
                }

            }
        } catch (InternalException | IllegalDiameterStateException | RouteException | OverloadException e) {
            logger.error("Send diameter request failed\n" + e.getMessage());

        }

        return clientData;
    }


    private ClientData parseDiameterResponse(Future<Message> response){
        Avp avp = null;
        AvpSet avpSet = null;
        ClientData clientData = null;

        try {
            avpSet = response.get().getAvps(); //получаем все AVP ответа
            avp = avpSet.getAvp(Avp.CHECK_BALANCE_RESULT);


            if(avp != null){
                //if AVP with balance is not empty
                clientData = new ClientData();
                clientData.setBalance(avp.getUTF8String());
                avp = avpSet.getAvp(Avp.TIME_STAMPS);
                clientData.setDate(avp.getTime());

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

                    if(avp.getInteger32() == ErrorCode.USER_NOT_FOUND){
                        logger.warn("User does not signup in system");
                    }
                }
            }
        } catch (AvpDataException | InterruptedException | ExecutionException | NullPointerException e){
            logger.error("Balance was not received\n" + e.getMessage());

        }

        return clientData;
    }


    private JDiameterRequest(){}

}
