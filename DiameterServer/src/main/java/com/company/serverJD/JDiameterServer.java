package com.company.serverJD;


import org.apache.logging.log4j.*;
import org.jdiameter.api.*;
import org.jdiameter.server.impl.StackImpl;
import org.jdiameter.server.impl.helpers.XMLConfiguration;


public class JDiameterServer {

    private static final Logger logger = LogManager.getLogger(JDiameterServer.class);

    public void startServer(){
        StackImpl server = new StackImpl();

        try{
            server.init(new XMLConfiguration("server-jdiameter-config.xml"));
            server.unwrap(Network.class).addNetworkReqListener(new Router(), ApplicationId.createByAuthAppId(33333)); //указываем слушателя (new ServerJD)
            server.start();

            System.out.println("Server started");
            logger.info("Server started [Router]");
        } catch (Exception e) {
            logger.error("Diameter server was not started\n" + e.getMessage());
        }


    }
}

