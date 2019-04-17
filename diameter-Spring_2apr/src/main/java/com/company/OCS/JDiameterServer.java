package com.company.OCS;

import org.jdiameter.api.*;
import org.jdiameter.server.impl.StackImpl;
import org.jdiameter.server.impl.helpers.XMLConfiguration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class JDiameterServer {
    public void startServer(){
        StackImpl server = new StackImpl();

        try{
            server.init(new XMLConfiguration("server-jdiameter-config.xml"));

            server.unwrap(Network.class).addNetworkReqListener(new NetworkReqListener() {
                @Override
                public Answer processRequest(Request request) {
                    System.out.println("запрос пришел");
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
                    answer.getAvps().addAvp(2, clientID.getBytes()); //положили баланс в AVP
                    answer.getAvps().addAvp(3, balanceBytes); //положили баланс в AVP
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









int i = 0;
Map<String, Integer> map = new ConcurrentHashMap<>();
    private String getBalanceForUser(String clientID){
//        Integer clientBalance = map.get(clientID);
//        if(clientBalance != null){
//            clientBalance++;
//            map.put(clientID, clientBalance);
//        }else{
//            map.put(clientID, 1);
//        }
//
////        bal++;
////        String balance = bal+"";
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //query to Cassandra
        //пока что без API между касандрой и этим кодом
        //касандру еще не добавили

        //именно здесь будет осуществляться получение баланса

        //return map.get(clientID).toString();

        i++;
        return i+"";
    }

//    private void onePeerIsConnected() throws Exception {
//        List<Peer> peers = server.unwrap(PeerTable.class).getPeerTable();
//        assertEquals(1,peers.size());
//    }
}

