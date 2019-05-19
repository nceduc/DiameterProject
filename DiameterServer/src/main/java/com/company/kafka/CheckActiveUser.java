package com.company.kafka;

import org.apache.logging.log4j.*;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

/**
 * Это класс необходим для удаления неактивных пользователей из Map с целью оптимизации использования памяти
 * */

public class CheckActiveUser extends TimerTask{

    private static final Logger logger = LogManager.getLogger(CheckActiveUser.class);

    @Override
    public void run() {
        Iterator<Map.Entry<String, ClientData>> it = ProcessKafkaListener.mapData.entrySet().iterator();
        Date date = new Date();
        String clientID = null;
        long time = 0L;


        while (it.hasNext()) {
            Map.Entry<String, ClientData> pair = it.next();

            clientID = pair.getKey(); //получаем номер пользователя
            time = pair.getValue().getDate().getTime(); //получаем ClientData, затем Date, затем время с 1970г
            logger.info("Check user: "+clientID);

            if((date.getTime() - time) > (60000*60*24)){
                it.remove();
                //KafkaProcessor.mapData.remove(clientID); вызывает исключение из-за потокобезопасности
                logger.info("User with number :"+clientID+" deleted from MAP");
            }
        }

    }
}

