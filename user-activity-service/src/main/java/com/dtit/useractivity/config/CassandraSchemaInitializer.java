package com.dtit.useractivity.config;

import com.datastax.oss.driver.api.core.CqlSession;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CassandraSchemaInitializer {

    private final CqlSession session;

    @PostConstruct
    public void initializeSchema() {
        String cql = """
            CREATE TABLE IF NOT EXISTS user_recently_viewed (
              user_email TEXT,
              viewed_at TIMESTAMP,
              product_id TEXT,
              PRIMARY KEY (user_email, viewed_at)
            ) WITH CLUSTERING ORDER BY (viewed_at DESC)
              AND default_time_to_live = 604800;
        """;
        session.execute(cql);
    }
}
