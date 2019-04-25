package com.company;

import com.company.Kafka.KafkaProcessor;
import com.company.ServerJD.JDiameterServer;
import org.apache.log4j.Logger;


public class Main {
	public static void main(String[] args) {
		JDiameterServer jDiameterServer = new JDiameterServer();
		KafkaProcessor kafkaProcessor = new KafkaProcessor();

		jDiameterServer.startServer(); //для принятия диаметрового запроса для получения баланса
		kafkaProcessor.start();
	}
}
