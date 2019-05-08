package com.company.Balance;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.System.out;


public class CassandraConnector {

    private static final Logger logger = LogManager.getLogger(CassandraConnector.class);

    public Session connect() {
        Cluster cluster = null;
        Session session = null;
        cluster = Cluster.builder().addContactPoint("localhost").withPort(9042).build();
        if (cluster.getMetadata().checkSchemaAgreement()) {
            // schema is in agreement
            session = cluster.connect();
            logger.info("Connected to Cassandra");
            System.out.println("Connected to Cassandra");
        }
        return session;
    }

}