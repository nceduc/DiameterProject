package com.company;

import com.company.kafka.CheckActiveUser;
import com.company.kafka.ProcessKafkaListener;
import com.company.serverJD.JDiameterServer;
import com.company.failApps.CheckFailApps;

import java.util.Timer;


public class Main {


	public static void main(String[] args) {
		ProcessKafkaListener kafkaListener = null;
		Thread thread = null;
		Timer t = new Timer();
		JDiameterServer jDiameterServer = new JDiameterServer();

		t.scheduleAtFixedRate(new CheckActiveUser(), 0, 60000*60*24);
		t.scheduleAtFixedRate(new CheckFailApps(), 0, 3000);
		jDiameterServer.startServer();

		//first consumer
		kafkaListener = new ProcessKafkaListener("consumer1", "group1", "responseBalance");
		thread = new Thread(kafkaListener);
		thread.start();

	}
}
