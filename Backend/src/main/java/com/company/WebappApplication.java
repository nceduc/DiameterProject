package com.company;

import com.company.clientJD.JDiameterConnectServer;
import com.company.clientJD.JDiameterRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebappApplication {

	public static void main(String[] args) {
		JDiameterConnectServer.getInstance().connect();
		JDiameterRequest.getInstance();
		SpringApplication.run(WebappApplication.class, args);
	}
}
