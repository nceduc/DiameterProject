package com.company.kafka;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ProcessKafkaListener implements Runnable{

    private static final Logger logger = LogManager.getLogger(ProcessKafkaListener.class);
    public static Map<String, ClientData> mapData = new HashMap<>();
    private KafkaConsumer<String, byte[]> consumer = null;

    public ProcessKafkaListener(String consumer, String group, String topic){
        Properties prop = createConsumerConfig(consumer, group);
        this.consumer = new KafkaConsumer<>(prop);
        //подписываемся
        this.consumer.subscribe(Arrays.asList(topic));
        logger.info("Subscribed to topic: " + topic + " [Balance]");
        System.out.println("Subscribed to topic: " + topic);
    }


    @Override
    public void run(){
        String clientID = null;
        ClientData clientData = null;

        while (true) { //читаем топик responseBalance
            ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(2000));
            for (ConsumerRecord<String, byte[]> record : records){
                System.out.println(Thread.currentThread().getName());
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
                "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        return props;
    }


}
