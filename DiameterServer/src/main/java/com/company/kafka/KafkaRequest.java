package com.company.kafka;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class KafkaRequest {

    private static final Logger logger = LogManager.getLogger(KafkaRequest.class);

    private static KafkaProducer kafkaProducer = null;


    public static void writeRecord(String clientID){
        final String topicName = "requestBalance";
        ProducerRecord producerRecord = new ProducerRecord(topicName, clientID);
        kafkaProducer.send(producerRecord); //пишем запись в кафку
    }

    //set properties
    public static void setProperties(){
        Properties props = null;

        // конфигурация
        props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //сериализуем
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProducer = new KafkaProducer(props);
    }

}
