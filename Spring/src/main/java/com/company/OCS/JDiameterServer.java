package com.company.OCS;

import org.jdiameter.api.*;
import org.jdiameter.server.impl.StackImpl;
import org.jdiameter.server.impl.helpers.XMLConfiguration;


public class JDiameterServer {
    public void startServer(){
        StackImpl server = new StackImpl();

        try{
            server.init(new XMLConfiguration("server-jdiameter-config.xml"));

            server.unwrap(Network.class).addNetworkReqListener(new NetworkReqListener() {
                public Answer processRequest(Request request) {
                    System.out.println("Request received");
                    //этот метод вызывается при диаметровом запросе
                    String clientID = null;
                    byte[] balanceBytes;
                    Answer answer;


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
        String balance = bal+"";

        //query to Cassandra
        //пока что без API между касандрой и этим кодом
        //касандру еще не добавили

        //именно здесь будет осуществляться получение баланса

        return balance;
    }

//    private void onePeerIsConnected() throws Exception {
//        List<Peer> peers = server.unwrap(PeerTable.class).getPeerTable();
//        assertEquals(1,peers.size());
//    }
}
