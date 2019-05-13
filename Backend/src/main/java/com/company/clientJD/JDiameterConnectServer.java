package com.company.clientJD;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdiameter.api.*;
import org.jdiameter.server.impl.StackImpl;
import org.jdiameter.server.impl.helpers.XMLConfiguration;
import org.mobicents.diameter.dictionary.AvpDictionary;

import java.util.concurrent.TimeUnit;


public class JDiameterConnectServer {

    private static final Logger logger = LogManager.getLogger(JDiameterConnectServer.class);
    private static JDiameterConnectServer INSTANCE = null;
    private static Session connection = null;


    public static JDiameterConnectServer getInstance(){
        if(INSTANCE == null){
            INSTANCE = new JDiameterConnectServer();
        }
        return INSTANCE;
    }


    public Session connect(){
        try {
            if(connection == null){
                connection = getSessionFactory().getNewSession();
            }
        } catch (InternalException e) {
            logger.error("Connect with Diameter server failed!");
        }

        return connection;
    }


    private SessionFactory getSessionFactory(){
        SessionFactory sessionFactory = null;
        StackImpl client = new StackImpl();
        try{
            sessionFactory = client.init(new XMLConfiguration("client-jdiameter-config.xml"));
            client.unwrap(Network.class).addNetworkReqListener(new NetworkReqListener() {
                @Override
                public Answer processRequest(Request request) {
                    return null;
                }
            }, ApplicationId.createByAuthAppId(33333));

            client.start(Mode.ALL_PEERS,7000, TimeUnit.MILLISECONDS); //timeout for sending request

            logger.info("Client connected with diameter server [Backend]");
            System.out.println("\n\nConnected with Diameter-server\n\n");

        } catch (InternalException | IllegalDiameterStateException | ApplicationAlreadyUseException e) {
            logger.error("Client connection with diameter was failed\n" + e.getMessage());
        } catch (Exception e) {
            logger.error(e.getStackTrace());
        }

        return sessionFactory;
    }

    private JDiameterConnectServer(){}

}
