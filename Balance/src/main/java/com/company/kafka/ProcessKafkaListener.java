package com.company.kafka;


import com.company.balance.Balance;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scala.sys.Prop;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import static com.company.balance.Balance.*;

public class ProcessKafkaListener implements Runnable{

    private static final Logger logger = LogManager.getLogger(ProcessKafkaListener.class);
    private KafkaConsumer<String, String> consumer = null;


    public ProcessKafkaListener(String consumer, String group, String topic){
        Properties prop = createConsumerConfig(consumer, group);
        this.consumer = new KafkaConsumer<>(prop);
        //подписываемся
        this.consumer.subscribe(Arrays.asList(topic));
        logger.info("Subscribed to topic: " + topic + " [Balance]");
        System.out.println("Subscribed to topic: " + topic);
        KafkaRequest.setProperties();
    }


    @Override
    public void run(){
        String clientID = null;
        String balance = null;
        boolean isClientNotFound = false;
        Balance balanceCassandra = new Balance(); //создали экземпляр и установили connection


        while (true) { //читаем топик requestBalance
            ConsumerRecords<String, String> records = consumer.poll(2000);
            if(records.count() > 1000){
                for (ConsumerRecord<String, String> ignored : records){
                    //если записей в кафке слишком много
                    System.out.println(records.count());
                    System.out.println("Only read...");
                    logger.info("Only read:"+records.count()+" records");
                }
            }else{
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(records.count());
                    System.out.println("Read record. Thread: " + Thread.currentThread().getName());

                    try {
                        clientID = record.value(); // получаем клиентский ID
                        balance = balanceCassandra.getBalance(clientID); //получаем баланс

                        if (balance != null) {
                            isClientNotFound = false;
                        } else if (isCassandraRunning()) {
                            isClientNotFound = true;
                        } else {
                            connection = null;
                        }

                        KafkaRequest.writeRecord(clientID, balance, isClientNotFound); //запись в кафку

                    } catch (NullPointerException e) {
                        logger.error("Balance or clientID was not got\n" + e.getMessage());
                    }
                }
            }
        }
    }


    private static Properties createConsumerConfig(String consumer, String group){
        //конфигурация
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "30000");
        props.put("session.timeout.ms", "30000");
        props.put("auto.offset.reset", "earliest");
        props.put("group.id", group);
        props.put("client.id", consumer);
        props.put("max.poll.records", 3000);

        //десериализуем
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

}
