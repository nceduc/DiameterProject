package com.company.balance;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.System.out;


public class CassandraConnector {

    private static final Logger logger = LogManager.getLogger(CassandraConnector.class);
    private static CassandraConnector INSTANCE = null;


    Session connect() {
        Cluster cluster = null;
        Session session = null;
        cluster = Cluster.builder().addContactPoint("localhost")
                .withPort(9042)
                .withReconnectionPolicy(new ConstantReconnectionPolicy(1000))
                .build();
        if (cluster.getMetadata().checkSchemaAgreement()) {
            // schema is in agreement
            session = cluster.connect();
            logger.info("Connected to Cassandra");
            System.out.println("Connected to Cassandra");
        }
        return session;
    }

    private CassandraConnector(){}

    public static CassandraConnector getInstance(){
        if(INSTANCE == null){
            INSTANCE = new CassandraConnector();
        }
        return INSTANCE;
    }

}