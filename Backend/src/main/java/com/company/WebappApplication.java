package com.company;

import com.company.ClientJD.JDiameterConnectServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebappApplication {

	public static void main(String[] args) {
		JDiameterConnectServer.getInstance().connect();
		SpringApplication.run(WebappApplication.class, args);
	}
}
