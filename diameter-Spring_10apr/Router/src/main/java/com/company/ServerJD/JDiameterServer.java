package com.company.ServerJD;

import org.jdiameter.api.ApplicationAlreadyUseException;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.Network;
import org.jdiameter.server.impl.StackImpl;
import org.jdiameter.server.impl.helpers.XMLConfiguration;


public class JDiameterServer {
    public void startServer(){
        StackImpl server = new StackImpl();

        try{
            server.init(new XMLConfiguration("server-jdiameter-config.xml"));
            server.unwrap(Network.class).addNetworkReqListener(new Router(), ApplicationId.createByAuthAppId(33333)); //указываем слушателя (new ServerJD)
            server.start();

        }catch (IllegalDiameterStateException e) {
            e.printStackTrace();
        }catch (ApplicationAlreadyUseException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n\nServer has started\n\n");
    }
}

