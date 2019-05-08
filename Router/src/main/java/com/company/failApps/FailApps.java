package com.company.failApps;

import com.company.Kafka.ClientData;
import com.company.Kafka.KafkaProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FailApps {

    private static final Logger logger = LogManager.getLogger(FailApps.class);

    //флаги для идентификации неработающих приложений
    public static boolean isKafkaFail = false;
    public static boolean isCassandraFail = false;
    public static boolean isReloading = false;


    public static void reloadApps() {
        isReloading = true; //начинаем процесс перезапуска

        if (isKafkaFail) {
            //stop
            execCommand("systemctl stop kafka");
            //start
            execCommand("systemctl start kafka");
            execCommand("sleep 20"); //need time to start
            isKafkaFail = false;
        }

        if (isCassandraFail) {
            //stop
            execCommand("service cassandra stop");
            //start
            execCommand("service cassandra start");
            execCommand("sleep 20"); //need time to start
            System.out.println("\n\n\nCassandra ISSSS\n\n\n");
            isCassandraFail = false;
        }

        isReloading = false;
    }


    public static boolean appsRunning(){
        boolean result = false;
        if(!isKafkaFail && !isCassandraFail){
            result = true;
        }
        return result;
    }


    public static boolean runningCassandra(String clientID){
        boolean result = false;
        ClientData clientData = KafkaProcessor.mapData.get(clientID);

        try {
            if(clientData == null || !clientData.isCassandraFail()){
                result = true;
            }
        }catch (NullPointerException ex){
            logger.warn("Cassandra does not work");
        }
        return result;
    }


    private static void execCommand(String command){
        String line = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            while ((line = br.readLine()) != null)
                System.out.println("line: " + line);

            System.out.println ("exit: " + process.exitValue());
            process.destroy();
        } catch (IOException | InterruptedException e) {
            logger.error("Some problems with execute Linux command");
        }
    }
}
