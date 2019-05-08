package com.company.Kafka;


import com.company.Balance.Balance;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static com.company.Balance.Balance.*;

public class KafkaProcessor {

    private static final Logger logger = LogManager.getLogger(KafkaProcessor.class);

    public void start(){
        String clientID = null;
        String balance = null;
        boolean isCassandraFail = false;
        Balance balanceCassandra = new Balance(); //создали экземпляр и установили connection
        KafkaConsumer<String, String> consumer = new KafkaListener().getConsumer(); //получаем подписчика (слушателя)


        while (true) { //читаем топик requestBalance
            ConsumerRecords<String, String> records = consumer.poll(1000);
            if(records.count() > 1000){
                for (ConsumerRecord<String, String> ignored : records){
                    //если записей в кафке слишком много
                    System.out.println(records.count());
                    System.out.println("Only read...");
                    logger.info("Only read:"+records.count()+" records");
                }
            }else{
                for (ConsumerRecord<String, String> record : records){
                    System.out.println(records.count());
                    System.out.println("Read record");

                    try {
                        clientID = record.value(); // получаем клиентский ID
                        if (balanceCassandra.isCassandraRunning()) {
                            balance = balanceCassandra.getBalance(clientID); //получаем баланс
                            isCassandraFail = false;
                        } else {
                            balance = null;
                            connection = null;
                            isCassandraFail = true;
                        }

                        KafkaRequest.writeRecordKafka(clientID, balance, isCassandraFail); //запись в кафку

                    }catch (NullPointerException e){
                        logger.error("Balance or clientID was not got\n" + e.getMessage());
                    }
                }
            }
        }
    }
}
