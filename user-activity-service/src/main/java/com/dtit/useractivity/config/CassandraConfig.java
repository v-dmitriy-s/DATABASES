package com.dtit.useractivity.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class CassandraConfig {

    @Value("${spring.cassandra.keyspace-name}")
    private String keyspaceName;

    @Value("${spring.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.cassandra.local-datacenter}")
    private String localDatacenter;

    @Value("${spring.cassandra.replication-config}")
    private String replicationConfig = "{'class':'SimpleStrategy','replication_factor':1}";

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer() {
        return builder -> {
            // Create keyspace if it doesn't exist
            try (CqlSession tempSession = CqlSession.builder()
                    .addContactPoint(parseContactPoint(contactPoints))
                    .withLocalDatacenter(localDatacenter)
                    .build()) {

                String query = String.format(
                        "CREATE KEYSPACE IF NOT EXISTS %s WITH REPLICATION = %s;",
                        keyspaceName, replicationConfig
                );
                tempSession.execute(query);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to create keyspace: " + keyspaceName, e);
            }

            // Set keyspace for the main application session
            builder.withKeyspace(keyspaceName);
        };
    }

    private InetSocketAddress parseContactPoint(String contactPoint) {
        String[] parts = contactPoint.split(":");
        String host = parts[0];
        int port = (parts.length > 1) ? Integer.parseInt(parts[1]) : 9042;
        return new InetSocketAddress(host, port);
    }
}
