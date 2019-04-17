package com.company.Kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

class KafkaListener {

    private KafkaConsumer<String, String> consumer;

    KafkaListener(){
        final String topicName = "getBalance";
        final String groupId = "group01";
        final String clientId = "client01";
        Properties props;


        //конфигурация
        props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", groupId);
        props.put("client.id", clientId);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");

        //десериализуем
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");


        consumer = new KafkaConsumer<>(props);

        //подписываемся
        consumer.subscribe(Arrays.asList(topicName));
        System.out.println("Subscribed to topic=" + topicName);

    }


    KafkaConsumer<String, String> getConsumer() {
        return consumer;
    }
}
