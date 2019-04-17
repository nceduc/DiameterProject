package com.cassandra.utils;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

import static java.lang.System.out;

/**
 * Class used for connecting to Cassandra database.
 */
public class CassandraConnector
{
    /** Cassandra Cluster. */
    private Cluster cluster;

    /** Cassandra Session. */
    private Session session;

    private final String node = "localhost";
    private final int port = 9042;

    public CassandraConnector()
    {
        this.cluster = Cluster.builder().addContactPoint(node).withPort(port).build();
        final Metadata metadata = cluster.getMetadata();
        out.printf("Connected to cluster: %s\n", metadata.getClusterName());
        for (final Host host : metadata.getAllHosts())
        {
            out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
                    host.getDatacenter(), host.getAddress(), host.getRack());
        }
        System.out.println("Connecting to IP Address " + node + ":" + port + "...");
        session = cluster.connect();
    }

    /**
     * Provide my Session.
     *
     * @return My session.
     */
    public Session getSession()
    {
        return this.session;
    }

    /** Close cluster. */
    public void close()
    {
        cluster.close();
    }
}