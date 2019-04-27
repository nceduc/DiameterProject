package com.company.Kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaRequest {

    private static final Logger logger = Logger.getLogger(KafkaRequest.class);

    public boolean writeRecordKafka(String clientID){
        final String topicName = "requestBalance";
        boolean result = false;
        Properties props;
        KafkaProducer producer;
        ProducerRecord producerRecord;
        Future<RecordMetadata> response;


        //конфигурация
        props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put("max.block.ms", 2000L);
        //сериализуем
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");


        producer = new KafkaProducer(props);
        producerRecord = new ProducerRecord(topicName, clientID);


        for(int i = 0; i < 3; i++){
            response = producer.send(producerRecord); //пишем запись в кафку
            try {
                if(response.get().hasOffset()){
                    result = true;
                    break;
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Write to Kafka error, try again... [KafkaRequest.class]\n" + e.getMessage());
            }
        }


        return result;
    }


}
