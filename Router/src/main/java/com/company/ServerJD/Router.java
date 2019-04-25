package com.company.ServerJD;

import com.company.Kafka.KafkaProcessor;
import com.company.Kafka.KafkaRequest;
import org.apache.log4j.Logger;
import org.jdiameter.api.*;

import java.util.HashMap;
import java.util.Map;
public class Router implements NetworkReqListener {

    private static final Logger logger = Logger.getLogger(Router.class);

    private KafkaRequest kafkaRequest = new KafkaRequest(); //для записей в кафку
    private static Map<String, String> dictionary = new HashMap<String, String>();

    static {
        //init dictionary
        initDictionary();
        System.out.println("Словарь загружен");
        logger.info("Dictionary loaded");
    }



    @Override
    public Answer processRequest(Request request) {

        System.out.println("Запрос пришел с BE");
        //этот метод вызывается при диаметровом запросе
        long codeDiameterAppId;
        int codeDiameterRequest;
        String appName;
        String requestName;
        String clientID = null;
        String balance = null;
        Answer answer = null;


        codeDiameterAppId = request.getApplicationId(); //получаем код диаметрового приложения
        codeDiameterRequest = request.getCommandCode(); //получаем код диаметрового запроса

        if(codeDiameterRequest == CodeRequest.GET_BALANCE_REQUEST){
            //this is Get-Balance-Request
            AvpSet avpSet = request.getAvps(); //получаем все Avp реквеста

            try {
                clientID = avpSet.getAvp(Avp.USER_IDENTITY).getUTF8String(); //получаем clientID для запроса баланса
            } catch (AvpDataException e) {
                logger.error("AVP is invalid [Router.class]\n" + e.getMessage());
            } catch (NullPointerException e) {
                logger.error("ClientID was not received [Router.class]\n" + e.getMessage());
            }

            System.out.println(clientID);
            kafkaRequest.writeRecordKafka(clientID); //пишем запись в кафку для получения баланса


            try{
                balance = KafkaProcessor.mapBalance.get(clientID); //получили баланс
                answer = request.createAnswer(); //начинаем формировать ответ для BE
                answer.getAvps().addAvp(Avp.CHECK_BALANCE_RESULT, balance.getBytes()); //положили баланс в ответ
            }catch (NullPointerException e){
                logger.error("Balance was not received [Router.class\n" + e.getMessage());
            }

        }else{
            //this is undefined request
            appName = getAppName(codeDiameterAppId);
            requestName = getRequestName(codeDiameterRequest);
            logger.warn("Such type request is not supported by this diameter-server [ AppName:"+appName+" RequestName:"+requestName+" ]");
        }

        return answer;
    }





    private String getAppName(long codeDiameterAppId){
        String appName;

        switch ((int) codeDiameterAppId) {
            case (1) : {
                appName = "Diameter Network Access Server Application";
                break;
            }

            case (4) : {
                appName = "Diameter Credit-Control Application";
                break;
            }

            case (5) : {
                appName = "Diameter Extensible Authentication Protocol Application";
                break;
            }

            case (6) : {
                appName = "Diameter Session Initiation Protocol Application";
                break;
            }

            case (33333) : {
                appName = "Diameter Control Balance Application";
                break;
            }

            default : {
                appName = "Undefined Application";
                break;
            }
        }

        return appName;
    }

    private String getRequestName(int codeDiameterRequest){
        String requestName;
        requestName = dictionary.get(codeDiameterRequest+"");
        return requestName;
    }

    private static void initDictionary(){
        //commandCode : nameRequest

        dictionary.put("272", "Credit-Control-Request");
        //Diameter Credit-Control Application
        //Credit-Control-Request


        //Message Format
        //
        //      <Credit-Control-Request> ::= < Diameter Header: 272, REQ, PXY >
        //                                   < Session-Id >
        //                                   { Origin-Host }
        //                                   { Origin-Realm }
        //                                   { Destination-Realm }
        //                                   { Auth-Application-Id }
        //                                   { Service-Context-Id }
        //                                   { CC-Request-Type }
        //                                   { CC-Request-Number }
        //                                   [ Destination-Host ]
        //                                   [ User-Name ]
        //                                   [ CC-Sub-Session-Id ]
        //                                   [ Acct-Multi-Session-Id ]
        //                                   [ Origin-State-Id ]
        //                                   [ Event-Timestamp ]
        //                                  *[ Subscription-Id ]
        //                                   [ Service-Identifier ]
        //                                   [ Termination-Cause ]
        //                                   [ Requested-Service-Unit ]
        //                                   [ Requested-Action ]
        //                                  *[ Used-Service-Unit ]
        //                                   [ Multiple-Services-Indicator ]
        //                                  *[ Multiple-Services-Credit-Control ]
        //                                  *[ Service-Parameter-Info ]
        //                                   [ CC-Correlation-Id ]
        //                                   [ User-Equipment-Info ]
        //                                  *[ Proxy-Info ]
        //                                  *[ Route-Record ]


        dictionary.put("268", "Extensible-Authentication-Protocol-Request");
        //Diameter Extensible Authentication Protocol Application (EAP)
        //Diameter-EAP-Request


        //Message format
        //
        //      <Diameter-EAP-Request> ::= < Diameter Header: 268, REQ, PXY >
        //                                 < Session-Id >
        //                                 { Auth-Application-Id }
        //                                 { Origin-Host }
        //                                 { Origin-Realm }
        //                                 { Destination-Realm }
        //                                 { Auth-Request-Type }
        //                                 [ Destination-Host ]
        //                                 [ NAS-Identifier ]
        //                                 [ NAS-IP-Address ]
        //                                 [ NAS-IPv6-Address ]
        //                                 [ NAS-Port ]
        //                                 [ NAS-Port-Id ]
        //                                 [ NAS-Port-Type ]
        //                                 [ Origin-State-Id ]
        //                                 [ Port-Limit ]
        //                                 [ User-Name ]
        //                                 { EAP-Payload }
        //                                 [ EAP-Key-Name ]
        //                                 [ Service-Type ]
        //                                 [ State ]
        //                                 [ Authorization-Lifetime ]
        //                                 [ Auth-Grace-Period ]
        //                                 [ Auth-Session-State ]
        //                                 [ Callback-Number ]
        //                                 [ Called-Station-Id ]
        //                                 [ Calling-Station-Id ]
        //                                 [ Originating-Line-Info ]
        //                                 [ Connect-Info ]
        //                               * [ Framed-Compression ]
        //                                 [ Framed-Interface-Id ]
        //                                 [ Framed-IP-Address ]
        //                               * [ Framed-IPv6-Prefix ]
        //                                 [ Framed-IP-Netmask ]
        //                                 [ Framed-MTU ]
        //                                 [ Framed-Protocol ]
        //                               * [ Tunneling ]
        //                               * [ Proxy-Info ]
        //                               * [ Route-Record ]


        dictionary.put("265", "Authorize-Authenticate-Request");
        //Diameter Network Access Server Application
        //AA-Request


        //Message Format
        //
        //         <AA-Request> ::= < Diameter Header: 265, REQ, PXY >
        //                          < Session-Id >
        //                          { Auth-Application-Id }
        //                          { Origin-Host }
        //                          { Origin-Realm }
        //                          { Destination-Realm }
        //                          { Auth-Request-Type }
        //                          [ Destination-Host ]
        //                          [ NAS-Identifier ]
        //                          [ NAS-IP-Address ]
        //                          [ NAS-IPv6-Address ]
        //                          [ NAS-Port ]
        //                          [ NAS-Port-Id ]
        //                          [ NAS-Port-Type ]
        //                          [ Origin-AAA-Protocol ]
        //                          [ Origin-State-Id ]
        //                          [ Port-Limit ]
        //                          [ User-Name ]
        //                          [ User-Password ]
        //                          [ Service-Type ]
        //                          [ State ]
        //                          [ Authorization-Lifetime ]
        //                          [ Auth-Grace-Period ]
        //                          [ Auth-Session-State ]
        //                          [ Callback-Number ]
        //                          [ Called-Station-Id ]
        //                          [ Calling-Station-Id ]
        //                          [ Originating-Line-Info ]
        //                          [ Connect-Info ]
        //                          [ CHAP-Auth ]
        //                          [ CHAP-Challenge ]
        //                        * [ Framed-Compression ]
        //                          [ Framed-Interface-Id ]
        //                          [ Framed-IP-Address ]
        //                        * [ Framed-IPv6-Prefix ]
        //                          [ Framed-IP-Netmask ]
        //                          [ Framed-MTU ]
        //                          [ Framed-Protocol ]
        //                          [ ARAP-Password ]
        //                          [ ARAP-Security ]
        //                        * [ ARAP-Security-Data ]
        //                        * [ Login-IP-Host ]
        //                        * [ Login-IPv6-Host ]
        //                          [ Login-LAT-Group ]
        //                          [ Login-LAT-Node ]
        //                          [ Login-LAT-Port ]
        //                          [ Login-LAT-Service ]
        //                        * [ Tunneling ]
        //                        * [ Proxy-Info ]
        //                        * [ Route-Record ]


        dictionary.put("283", "User-Authorization-Request");
        //Diameter Session Initiation Protocol Application
        //User-Authorization-Request


        //Message Format
        //
        //       <UAR> ::= < Diameter Header: 283, REQ, PXY >
        //                 < Session-Id >
        //                 { Auth-Application-Id }
        //                 { Auth-Session-State }
        //                 { Origin-Host }
        //                 { Origin-Realm }
        //                 { Destination-Realm }
        //                 { SIP-AOR }
        //                 [ Destination-Host ]
        //                 [ User-Name ]
        //                 [ SIP-Visited-Network-Id ]
        //                 [ SIP-User-Authorization-Type ]
        //               * [ Proxy-Info ]
        //               * [ Route-Record ]


        dictionary.put("3000", "Get-Balance-Request");
        //Diameter Control Balance Application
        //Get-Balance-Request


        //Message Format
        //
        //       <GBR> ::= < Diameter Header: 3000, REQ, PXY >
        //                 [ User-Identity ]
    }

}
