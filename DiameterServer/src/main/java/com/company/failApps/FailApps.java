package com.company.failApps;

public class FailApps {

    private static FailApps INSTANCE = null;

    //флаги для идентификации неработающих приложений
    private boolean isKafkaFail = false;
    private boolean isCassandraFail = false;


    public boolean appsRunning(){
        boolean result = false;
        if(!isKafkaFail && !isCassandraFail){
            result = true;
        }
        return result;
    }


    public static FailApps getInstance(){
        if(INSTANCE == null){
            INSTANCE = new FailApps();
        }
        return INSTANCE;
    }


    public boolean isKafkaFail() {
        return isKafkaFail;
    }

    public void setKafkaFail(boolean kafkaFail) {
        isKafkaFail = kafkaFail;
    }

    public boolean isCassandraFail() {
        return isCassandraFail;
    }

    public void setCassandraFail(boolean cassandraFail) {
        isCassandraFail = cassandraFail;
    }


    private FailApps(){}

}
