package com.dtit.inventory.services;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.dtit.inventory.model.InventoryItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final DynamoDBMapper dynamoDBMapper;

    public List<InventoryItem> getStockByProduct(String productId) {
        InventoryItem probe = new InventoryItem();
        probe.setProductId(productId);

        DynamoDBQueryExpression<InventoryItem> queryExpression = new DynamoDBQueryExpression<InventoryItem>()
                .withHashKeyValues(probe);

        return dynamoDBMapper.query(InventoryItem.class, queryExpression);
    }

    public InventoryItem getStockAtLocation(String productId, String location) {
        return dynamoDBMapper.load(InventoryItem.class, productId, location);
    }

    public InventoryItem saveOrUpdate(InventoryItem item) {
        dynamoDBMapper.save(item);
        return item;
    }

    public List<InventoryItem> getAll() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return dynamoDBMapper.scan(InventoryItem.class, scanExpression);
    }

    public void deleteStock(String productId, String location) {
        InventoryItem item = new InventoryItem();
        item.setProductId(productId);
        item.setLocation(location);
        dynamoDBMapper.delete(item);
    }
}
