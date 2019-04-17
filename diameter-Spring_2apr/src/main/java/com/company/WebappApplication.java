package com.company;

import com.company.backend.JDiameterConnectServer;
import com.company.OCS.JDiameterServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebappApplication {

	public static void main(String[] args) {
		new JDiameterServer().startServer();
		new JDiameterConnectServer().connect();

		SpringApplication.run(WebappApplication.class, args);
	}
}
