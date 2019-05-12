package com.company.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Properties;

class KafkaListener {

    private static final Logger logger = LogManager.getLogger(KafkaListener.class);

    public KafkaConsumer<String, String> getConsumer() {
        final String topicName = "requestBalance";
        final String groupId = "group01";
        final String clientId = "client01";
        KafkaConsumer<String, String> consumer = null;
        Properties props = null;


        //конфигурация
        props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", groupId);
        props.put("client.id", clientId);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("max.poll.records", 3000);

        //десериализуем
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");


        consumer = new KafkaConsumer<>(props);

        //подписываемся
        consumer.subscribe(Arrays.asList(topicName));
        logger.info("Subscribed to topic: " + topicName + " [Balance]");
        System.out.println("Subscribed to topic: " + topicName);

        return consumer;
    }

}
