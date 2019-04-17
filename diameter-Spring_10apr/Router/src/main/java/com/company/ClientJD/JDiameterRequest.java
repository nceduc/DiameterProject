package com.company.ClientJD;


import org.jdiameter.api.*;


public class JDiameterRequest {

    public void returnBalance(String clientID, String balance){
        Session session = getSession();
        Request request = formDiameterRequest(session, clientID, balance);
        sendDiameterRequest(request, session);

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


    private Request formDiameterRequest(Session session, String clientID, String balance){
        Request request = null;

        if(session != null){
            request = session.createRequest(7, ApplicationId.createByAuthAppId(33333),"exchange.example.org","127.0.0.1"); //формируем запрос
            request.getAvps().addAvp(2, clientID.getBytes()); //положили клиентский ID в AVP
            request.getAvps().addAvp(3, balance.getBytes()); //положили баланс в AVP
        }
        return request;
    }


    private void sendDiameterRequest(Request request, Session session){

        try {
            session.send(request); //послали диаметровый запрос
            System.out.println("запрос ушел в BE");

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
    }


}
