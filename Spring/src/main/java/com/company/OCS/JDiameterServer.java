package com.company.OCS;
import com.company.backend.ControlRequest;
import org.apache.log4j.Logger;
import org.jdiameter.api.*;
import org.jdiameter.server.impl.StackImpl;
import org.jdiameter.server.impl.helpers.XMLConfiguration;


public class JDiameterServer {

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
//            e1.printStackTrace();
//        }
//    }


    public void startServer(){
        //launch server
        StackImpl server = new StackImpl();

        try{
            server.init(new XMLConfiguration("server-jdiameter-config.xml"));

            server.unwrap(Network.class).addNetworkReqListener(new NetworkReqListener() {
                public Answer processRequest(Request request) {
                    String clientID = null;
                    byte[] balanceBytes;
                    Answer answer;

                    System.out.println("Request received");


                    //парсим запрос
                    try {
                        clientID = request.getAvps().getAvp(1).getUTF8String();
                    } catch (AvpDataException e) {
                        e.printStackTrace();
                    }

                    //формирем ответ
                    balanceBytes = getBalanceForUser(clientID).getBytes(); //получаем баланс
                    answer = request.createAnswer();
                    answer.getAvps().addAvp(2, balanceBytes); //положили баланс в AVP
                    return answer;
                }
            }, ApplicationId.createByAuthAppId(33333));
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






int bal = 0;
    private String getBalanceForUser(String clientID){
        bal++;
        String balance = bal + "";
        //query to Cassandra


        return balance;
    }

//    private void onePeerIsConnected() throws Exception {
//        List<Peer> peers = server.unwrap(PeerTable.class).getPeerTable();
//        assertEquals(1,peers.size());
//    }
}
