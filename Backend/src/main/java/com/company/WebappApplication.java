package com.company;

import com.company.ClientJD.JDiameterConnectServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebappApplication {

	public static void main(String[] args) {
		JDiameterConnectServer jDiameterConnectServer = new JDiameterConnectServer();
		jDiameterConnectServer.connect(); //устанавливаем связь с сервером для запроса баланса

		SpringApplication.run(WebappApplication.class, args);
	}
}
