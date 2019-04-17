package com.company.Kafka;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import com.company.ClientJD.*;

public class KafkaProcessor {

   public void start(){

       String clientID;
       String balance;
       boolean connectServerBE = false; //флаг для соединения с сервером BE
       JDiameterRequest jDiameterRequest = new JDiameterRequest(); //для возвращение баланса через диаметр
       KafkaListener kafkaListener = new KafkaListener(); //запустили слушателя кафки баланса
       KafkaConsumer<String, String> consumer = kafkaListener.getConsumer(); //получаем подписчика для получения баланса

        while (true) { //читаем топик returnBalance
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records){
                if(!connectServerBE){
                    new JDiameterConnectServer().connect(); //соединились с сервером с BE, для возвращение баланса юзеру
                    connectServerBE = true;
                }
                clientID = record.key();
                balance = record.value();
                System.out.println(clientID);
                System.out.println(balance);
                jDiameterRequest.returnBalance(clientID, balance); //возвратили баланс в BE
            }
        }
    }
}
