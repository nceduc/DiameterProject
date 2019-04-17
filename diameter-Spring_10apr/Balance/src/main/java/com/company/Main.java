package com.company;

import com.company.Kafka.KafkaProcessor;

public class Main {
    public static void main(String[] args) {
        KafkaProcessor kafkaProcessor = new KafkaProcessor();
        kafkaProcessor.start();
    }
}