package com.company.Kafka;

import com.company.Balance.Balance;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class KafkaProcessor {

    public void start(){

        String clientID;
        String balance;
        KafkaListener kafkaListener = new KafkaListener(); //запускаем слушателя
        KafkaConsumer<String, String> consumer = kafkaListener.getConsumer(); //получаем подписчика
        KafkaRequest kafkaRequest = new KafkaRequest(); //создаем один раз экземпляр для создания записей в кафке

        while (true) { //читаем топик getBalance
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records){
                clientID = record.value();
                System.out.println("Read record");
                balance = new Balance().getBalance(clientID); //получаем баланс
                kafkaRequest.sendRecordKafka(clientID, balance); //запись в кафку
            }
        }
    }
}
