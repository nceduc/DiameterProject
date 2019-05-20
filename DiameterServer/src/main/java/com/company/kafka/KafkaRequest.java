package com.company.kafka;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class KafkaRequest {

    private static final Logger logger = LogManager.getLogger(KafkaRequest.class);

    public static KafkaProducer pr = null;

    public static KafkaProducer getProp(){
        final String topicName = "requestBalance";
        boolean result = false;
        Properties props = null;
        KafkaProducer producer = null;
        ProducerRecord producerRecord = null;


        //конфигурация
        props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put("max.block.ms", 10000L);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        //сериализуем
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");


        if(pr == null){
            producer = new KafkaProducer(props);
        }
        return producer;
    }

}
