package com.company.Kafka;



import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KafkaProcessor {

   public static Map<String, ClientData> mapData = new HashMap<>();

   public void start(){

       String clientID;
       String balance;
       ClientData clientData;
       KafkaConsumer<String, String> consumer = new KafkaListener().getConsumer(); //получаем подписчика для получения баланса

       while (true) { //читаем топик responseBalance
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records){
                clientID = record.key();
                balance = record.value();

                clientData = new ClientData();
                clientData.setBalance(balance);
                clientData.setDate(new Date());
                mapData.put(clientID, clientData);
            }
        }
    }
}
