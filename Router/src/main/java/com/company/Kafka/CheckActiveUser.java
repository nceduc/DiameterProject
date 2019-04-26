package com.company.Kafka;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

/**
 * Это класс необходим для удаления неактивных пользователей из Map с целью оптимизации использования памяти
 * */

public class CheckActiveUser extends TimerTask{

    @Override
    public void run() {
        Iterator<Map.Entry<String, ClientData>> it = KafkaProcessor.mapData.entrySet().iterator();
        Date date = new Date();
        String clientID;
        long time;


        while (it.hasNext()) {
            Map.Entry<String, ClientData> pair = it.next();

            clientID = pair.getKey(); //получаем номер пользователя
            time = pair.getValue().getDate().getTime(); //получаем ClientData, затем Date, затем время с 1970г
            System.out.println("check:"+clientID);
            System.out.println(date.getTime() - time);
            if((date.getTime() - time) > (60000*60*24)){
                it.remove();
                //KafkaProcessor.mapData.remove(clientID); вызывает исключение из-за потокобезопасности
                System.out.println("Deleted: "+clientID );
            }
        }

    }
}

