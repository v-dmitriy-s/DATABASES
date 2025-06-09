package com.dtit.useractivity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;

@Table("user_recently_viewed")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivity {

    @PrimaryKeyColumn(name = "user_email", type = PrimaryKeyType.PARTITIONED)
    private String userEmail;

    @PrimaryKeyColumn(name = "viewed_at", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private Instant viewedAt;

    @Column("product_id")
    private String productId;

}
