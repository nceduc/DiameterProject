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
        final Metadata metadata = cluster.getMetadata();
        logger.info("Connected to cluster: %s\n" + metadata.getClusterName());

        for (final Host host : metadata.getAllHosts())
        {
            logger.info("Datacenter:"+host.getDatacenter()+" Host:"+ host.getAddress()+" Rack:"+host.getRack());
            out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
                    host.getDatacenter(), host.getAddress(), host.getRack());
        }
        logger.info("Connected to Cassandra");
        System.out.println("Connected to Cassandra");
        session = cluster.connect();
        return session;
    }

}