package com.company.ClientJD;

import org.apache.log4j.Logger;
import org.jdiameter.api.*;
import org.jdiameter.server.impl.StackImpl;
import org.jdiameter.server.impl.helpers.XMLConfiguration;

import java.util.concurrent.TimeUnit;


public class JDiameterConnectServer {

    private static final Logger logger = Logger.getLogger(JDiameterConnectServer.class);
    static SessionFactory connectionSession;

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

            logger.info("Client connected with diameter server [REST]");
            System.out.println("\n\nConnected with server\n\n");

        } catch (InternalException | IllegalDiameterStateException | ApplicationAlreadyUseException e) {
            logger.error("Client connection with diameter connection was failed [JDiameterConnectServer.class]\n" + e.getMessage());
        } catch (Exception e) {
            logger.error(e.getStackTrace());
        }


    }
}
