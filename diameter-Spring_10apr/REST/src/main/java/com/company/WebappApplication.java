package com.company;

import com.company.ClientJD.JDiameterConnectServer;
import com.company.ServerJD.JDiameterServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebappApplication {

	public static void main(String[] args) {
		JDiameterConnectServer jDiameterConnectServer = new JDiameterConnectServer();
		JDiameterServer jDiameterServer = new JDiameterServer();

		jDiameterConnectServer.connect(); //устанавливаем связь с сервером для запроса баланса
		jDiameterServer.startServer();//запускаем сервер для получения баланса через диаметровый запрос

		SpringApplication.run(WebappApplication.class, args);
	}
}
