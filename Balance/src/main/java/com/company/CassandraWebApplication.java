package com.company;

import com.company.kafka.ProcessKafkaListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CassandraWebApplication {

	public static void main(String[] args) {
		ProcessKafkaListener kafkaListener = null;
		Thread thread = null;
		SpringApplication.run(CassandraWebApplication.class, args);

		//first consumer
		kafkaListener = new ProcessKafkaListener("consumer1", "group1", "requestBalance");
		thread = new Thread(kafkaListener);
		thread.start();

		//second consumer
		kafkaListener = new ProcessKafkaListener("consumer2", "group1", "requestBalance");
		thread = new Thread(kafkaListener);
		thread.start();

	}
}
