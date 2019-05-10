package com.company.Kafka;


import com.company.failApps.FailApps;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class KafkaProcessor {


   private static final Logger logger = LogManager.getLogger(KafkaProcessor.class);
   public static Map<String, ClientData> mapData = new HashMap<>();


   public void start(){
       String clientID = null;
       ClientData clientData = null;
       KafkaConsumer<String, byte[]> consumer = new KafkaListener().getConsumer(); //получаем подписчика для получения баланса



       while (true) { //читаем топик responseBalance
           ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(1000));
           for (ConsumerRecord<String, byte[]> record : records){
               clientID = record.key();
               clientData = (ClientData) deserialize(record.value());
               if(clientData.getBalance() != null || clientData.isClientNotFound()){
                   mapData.put(clientID, clientData);
               }
           }
       }
   }

   private static Object deserialize(byte[] bytes){
       Object object = null;
       ByteArrayInputStream array = null;
       ObjectInputStream objStream = null;
       try{
           array = new ByteArrayInputStream(bytes);
           objStream = new ObjectInputStream(array);
           object = objStream.readObject();
       } catch (IOException | ClassNotFoundException e) {
           logger.warn("Deserialization failed [Router]\n" + e.getMessage());
       }
       return object;
    }

}
