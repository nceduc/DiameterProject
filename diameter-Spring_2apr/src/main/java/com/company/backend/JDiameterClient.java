package com.company.backend;

import org.jdiameter.api.*;


class JDiameterClient {

    String getBalance(String clientID){
        Session session = getSession();
        Request request = formDiameterRequest(session, clientID);
        String balance = sendDiameterRequest(request, session, clientID);

        return balance;
    }

    private Session getSession(){
        Session session = null;

        try {
            session = JDiameterConnectServer.connectionSession.getNewSession();
        } catch (InternalException e) {
            e.printStackTrace();
        }
        return session;
    }


    private Request formDiameterRequest(Session session, String clientID){
        Request request = null;

        if(session != null){
            request = session.createRequest(7, ApplicationId.createByAuthAppId(33333),"exchange.example.org","127.0.0.1"); //формируем запрос
            request.getAvps().addAvp(1, clientID.getBytes()); //положили клиентский ID в AVP
        }
        return request;
    }


    private String sendDiameterRequest(Request request, Session session, String clientID){
        String balance = null;

        try {
            session.send(request, new JDiameterListener()); //послали диаметровый запрос
            System.out.println("запрос ушел");

            balance = JDiameterListener.mapBalance.get(clientID); //если баланс не будет найден, то balance = null;

        } catch (InternalException e) {
            e.printStackTrace();
        } catch (IllegalDiameterStateException e) {
            e.printStackTrace();
        } catch (RouteException e) {
            e.printStackTrace();
        } catch (OverloadException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }


        return balance;
    }


}
