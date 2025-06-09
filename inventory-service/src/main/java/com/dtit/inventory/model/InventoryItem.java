package com.dtit.inventory.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.dtit.inventory.utils.LocalDateTimeConverter;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@DynamoDBTable(tableName = "inventory")
public class InventoryItem {

    @DynamoDBHashKey(attributeName = "product_id")
    private String productId;

    @DynamoDBRangeKey(attributeName = "location")
    private String location;

    @DynamoDBAttribute(attributeName = "stock_count")
    private Integer stockCount;

    @DynamoDBAttribute(attributeName = "last_updated")
    private String lastUpdated;

    @DynamoDBAttribute(attributeName = "restock_threshold")
    private Integer restockThreshold;
}
