package com.company;

import com.company.Kafka.KafkaProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CassandraWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(CassandraWebApplication.class, args);

		KafkaProcessor kafkaProcessor = new KafkaProcessor();
		kafkaProcessor.start();

	}
}
