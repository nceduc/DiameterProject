package com.company.failApps;

import com.company.Kafka.KafkaProcessor;
import org.I0Itec.zkclient.ZkClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TimerTask;


/**
 * Этот класс необхожим для периодической проверки работы приложений
 * */

public class CheckFailApps extends TimerTask{

    private static final Logger logger = LogManager.getLogger(CheckFailApps.class);
    private static ZkClient zkClient = new ZkClient("localhost:2181", 10000); //for connect ZooKeeper
    private FailApps failApps = FailApps.getInstance();
    static  int count = 0;

    @Override
    public void run() {
        count++;
        if(!isKafkaRunning()) {
            //если кафка не работает
            logger.error("Kafka failed. Restart...");
            failApps.setKafkaFail(true);
        }

        if(!isCassandraRunning()) {
            //если кассандра не работает
            logger.error("Cassandra failed. Restart...");
            failApps.setCassandraFail(true);
        }


        if (failApps.isKafkaFail()) {
            //stop
            execCommand("service kafka stop");
            //start
            execCommand("service kafka start");

            logger.warn("Try to reload Kafka");
            while(true){
                if(isKafkaRunning()){
                    break;
                }
            }

            failApps.setKafkaFail(false);
        }

        if(failApps.isCassandraFail()){
            //stop
            execCommand("service cassandra stop");
            //start
            execCommand("service cassandra start");

            logger.warn("Try to reload Cassandra");
            while(true){
                if(isCassandraRunning()){
                    break;
                }
            }

            failApps.setCassandraFail(false);
        }
        System.out.println("Check number: "+count);
    }


    private static int execCommand(String command){
        int exitCode = -1;
        String line = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = br.readLine()) != null)
                //wait

            exitCode = process.exitValue();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            logger.error("Some problems with execute Linux command");
        }
        return exitCode;
    }


    private static boolean isKafkaRunning(){
        boolean result = false;
        int brokersCount = zkClient.countChildren("/brokers/ids");
        if(brokersCount > 0){
            result = true;
        }
        return result;
    }


    private static boolean isCassandraRunning(){
        boolean result = false;
        if(execCommand("nodetool status") == 0){
            result = true;
        }
        return result;
    }

}

