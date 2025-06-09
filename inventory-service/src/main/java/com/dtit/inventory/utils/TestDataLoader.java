package com.dtit.inventory.utils;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.dtit.inventory.model.InventoryItem;
import com.dtit.inventory.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {

    private final InventoryRepository inventoryRepository;
    private final AmazonDynamoDB dynamoDB;
    private final DynamoDBMapper mapper;

    @Override
    public void run(String... args) {
        createTableIfNotExists();

        IntStream.rangeClosed(1, 100).forEach(i -> {
            InventoryItem item = new InventoryItem();
            item.setProductId(String.valueOf(1000 + i));
            item.setLocation("default");
            item.setStockCount((int)(Math.random() * 50 + 1));
            item.setLastUpdated(LocalDateTime.now().toString());
            item.setRestockThreshold((int)(Math.random() * 100 + 1));
            inventoryRepository.save(item);
        });

        System.out.println("✅ Inserted 100 test inventory items");
    }

    private void createTableIfNotExists() {
        CreateTableRequest tableRequest = mapper.generateCreateTableRequest(InventoryItem.class);
        tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        ListTablesResult tables = dynamoDB.listTables();
        if (!tables.getTableNames().contains(tableRequest.getTableName())) {
            dynamoDB.createTable(tableRequest);
            System.out.println("✅ Created table: " + tableRequest.getTableName());
        } else {
            System.out.println("ℹ️ Table already exists: " + tableRequest.getTableName());
        }
    }
}
