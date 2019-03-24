package com.company.backend;

import org.apache.log4j.Logger;
import org.jdiameter.api.*;
import org.jdiameter.server.impl.StackImpl;
import org.jdiameter.server.impl.helpers.XMLConfiguration;

import java.util.concurrent.TimeUnit;

/**
 * Класс необходим для формирования диаметровых запросов к слушателю (JDiameterServer)
 */
public class JDiameterClient {
    public static SessionFactory clientSessionFactory;


    //for logging ************************
    private static final Logger LOG = Logger.getLogger(ControlRequest.class);
//    static{
//        InputStream inStreamLog4j;
//        try {
//            inStreamLog4j = new FileInputStream("log4j.properties");
//            Properties propertiesLog4j = new Properties();
//            try {
//                propertiesLog4j.load(inStreamLog4j);
//                PropertyConfigurator.configure(propertiesLog4j);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            LOG.debug("log4j configured");
//        } catch (FileNotFoundException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//    }


    public void startClient(){
        //launch client
        StackImpl client = new StackImpl();
        try{
            clientSessionFactory = client.init(new XMLConfiguration("client-jdiameter-config.xml"));
            client.unwrap(Network.class).addNetworkReqListener(new NetworkReqListener() {
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

        System.out.println("\n\nClient has started\n\n");
    }
}
