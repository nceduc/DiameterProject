package com.company.Kafka;


import com.company.Balance.Balance;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.util.Properties;

public class KafkaProcessor {

    private static final Logger logger = Logger.getLogger(KafkaProcessor.class);

    public void start(){

        String clientID = null;
        String balance = null;
        Balance balanceCassandra = new Balance(); //создали экземпляр и установили connection
        KafkaConsumer<String, String> consumer = new KafkaListener().getConsumer(); //получаем подписчика (слушателя)

        while (true) { //читаем топик requestBalance
            ConsumerRecords<String, String> records = consumer.poll(1000);
            if(records.count() > 2000){
                for (ConsumerRecord<String, String> record : records){
                    //если записей в кафке слишком много
                    System.out.println(records.count());
                    System.out.println("Only read...");
                    logger.info("Only read:"+records.count()+" records");

                }
            }else{
                for (ConsumerRecord<String, String> record : records){
                    System.out.println(records.count());
                    System.out.println("Read record");

                    try {
                        clientID = record.value(); // получаем клиентский ID
                        balance = balanceCassandra.getBalance(clientID); //получаем баланс
                    }catch (NullPointerException e){
                        logger.error("Balance or clientID was not got [KafkaProcessor.class]\n" + e.getMessage());
                    }
                    writeRecordKafka(clientID, balance); //запись в кафку
                }
            }
        }
    }


    private void writeRecordKafka(String clientID, String balance){
        String topicName = "responseBalance";
        Properties props;
        KafkaProducer producer;
        ProducerRecord producerRecord;


        // конфигурация
        props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //сериализуем
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");


        producer = new KafkaProducer(props);
        producerRecord = new ProducerRecord(topicName, clientID, balance);
        producer.send(producerRecord);
    }
}
