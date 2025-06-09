package com.dtit.inventory.repositories;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public abstract class CrudRepository<T, K, R> {

    @Autowired
    protected DynamoDBMapper dynamoDBMapper;
    private final Class<T> entityClass;

    public CrudRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public <S extends T> S save(S entity) {
        dynamoDBMapper.save(entity);
        return entity;
    }

    public Optional<T> findById(K key) {
        return Optional.ofNullable(dynamoDBMapper.load(entityClass, key));
    }

    public Optional<T> findById(K key, R rangeKey) {
        return Optional.ofNullable(dynamoDBMapper.load(entityClass, key, rangeKey));
    }

    public Optional<List<T>> findAll() {
        return Optional.ofNullable(dynamoDBMapper.scan(entityClass, null));
    }

    public Optional<List<T>> findBy(DynamoDBQueryExpression<T> queryExpression) {
        return Optional.ofNullable(dynamoDBMapper.query(entityClass, queryExpression));
    }

    public Optional<List<T>> findBy(DynamoDBScanExpression scanExpression) {
        return Optional.ofNullable(dynamoDBMapper.scan(entityClass, scanExpression));
    }

    public boolean exists(DynamoDBQueryExpression<T> queryExpression) {
        return dynamoDBMapper.count(entityClass, queryExpression) > 0;
    }

    public void delete(T entity) {
        dynamoDBMapper.delete(entity);
    }

}
