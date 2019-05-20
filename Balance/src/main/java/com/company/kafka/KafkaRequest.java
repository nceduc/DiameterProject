package com.company.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Properties;

class KafkaRequest {

    private static final Logger logger = LogManager.getLogger(KafkaRequest.class);

    private static KafkaProducer k = null;

    static KafkaProducer get(){
        final String topicName = "responseBalance";
        byte[] value = null;
        Properties props = null;
        ClientData clientData = null;
        ProducerRecord producerRecord = null;


        // конфигурация
        props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //сериализуем
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.ByteArraySerializer");


        if(k == null){

            k = new KafkaProducer(props);
        }
        return k;
    }


    private static byte[] serialize(Object obj){
        ByteArrayOutputStream array = null;
        ObjectOutputStream objStream = null;
        try{
            array = new ByteArrayOutputStream();
            objStream = new ObjectOutputStream(array);
            objStream.writeObject(obj);
        }catch (IOException ex){
            logger.warn("Serialization failed [Balance]\n" + ex.getMessage());
        }
        return array.toByteArray();
    }

}
