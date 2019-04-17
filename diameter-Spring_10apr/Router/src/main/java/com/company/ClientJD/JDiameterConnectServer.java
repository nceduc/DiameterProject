package com.company.ClientJD;

import org.jdiameter.api.*;
import org.jdiameter.server.impl.StackImpl;
import org.jdiameter.server.impl.helpers.XMLConfiguration;

import java.util.concurrent.TimeUnit;


public class JDiameterConnectServer {
    public static SessionFactory connectionSession;

    public void connect(){
        StackImpl client = new StackImpl();
        try{
            connectionSession = client.init(new XMLConfiguration("client-jdiameter-config.xml"));
            client.unwrap(Network.class).addNetworkReqListener(new NetworkReqListener() {
                @Override
                public Answer processRequest(Request request) {
                    return null;
                }
            }, ApplicationId.createByAuthAppId(33333));
            client.start(Mode.ALL_PEERS,2000,TimeUnit.MILLISECONDS); //timeout for sending request

        } catch (InternalException e) {
            e.printStackTrace();
        } catch (IllegalDiameterStateException e) {
            e.printStackTrace();
        } catch (ApplicationAlreadyUseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n\nConnected with server\n\n");
    }
}
