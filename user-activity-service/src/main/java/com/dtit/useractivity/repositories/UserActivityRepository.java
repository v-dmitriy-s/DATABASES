package com.dtit.useractivity.repositories;

import com.dtit.useractivity.model.UserActivity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;

public interface UserActivityRepository extends CassandraRepository<UserActivity, String> {
    @Query("SELECT * FROM user_recently_viewed WHERE user_email = ?0 LIMIT 10")
    List<UserActivity> findTop10ByUserEmail(String userEmail);
}
