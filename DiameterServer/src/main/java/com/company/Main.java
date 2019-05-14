package com.company;

import com.company.kafka.CheckActiveUser;
import com.company.kafka.KafkaProcessor;
import com.company.serverJD.JDiameterServer;
import com.company.failApps.CheckFailApps;

import java.util.Timer;


public class Main {


	public static void main(String[] args) {
		Timer t = new Timer();
		JDiameterServer jDiameterServer = new JDiameterServer();
		KafkaProcessor kafkaProcessor = new KafkaProcessor();

		//t.scheduleAtFixedRate(new CheckActiveUser(), 0, 60000*60*24);
		//t.scheduleAtFixedRate(new CheckFailApps(), 0, 1000);
		jDiameterServer.startServer();
		kafkaProcessor.start();
	}
}