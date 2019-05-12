package com.company.kafka;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class KafkaRequest {

    private static final Logger logger = LogManager.getLogger(KafkaRequest.class);

    public static void writeRecordKafka(String clientID){
        final String topicName = "requestBalance";
        boolean result = false;
        Properties props = null;
        KafkaProducer producer = null;
        ProducerRecord producerRecord = null;


        //конфигурация
        props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put("max.block.ms", 10000L);
        //сериализуем
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");


        producer = new KafkaProducer(props);
        producerRecord = new ProducerRecord(topicName, clientID);
        producer.send(producerRecord); //пишем запись в кафку
    }

}
