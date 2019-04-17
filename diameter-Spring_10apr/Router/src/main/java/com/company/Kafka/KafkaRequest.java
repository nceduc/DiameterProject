package com.company.Kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

public class KafkaRequest {

    public void sendRecordKafka(String clientID){
        String topicName = "getBalance";
        Properties props;
        KafkaProducer producer;
        ProducerRecord producerRecord;


        //конфигурация
        props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //сериализуем
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");


        producer = new KafkaProducer(props);
        producerRecord = new ProducerRecord(topicName, clientID);
        producer.send(producerRecord);
    }


}
