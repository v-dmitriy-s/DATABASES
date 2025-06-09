package com.dtit.product.utils;

import com.dtit.product.clients.InventoryClient;
import com.dtit.product.clients.InventoryItem;
import com.dtit.product.model.Product;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@UtilityClass
public class CheckUtil {

    public static boolean checkInStock(Product product, InventoryClient inventoryClient, Logger log) {
        AtomicBoolean inStock = new AtomicBoolean(false);
        try {
            List<InventoryItem> stockList = inventoryClient.getStockByProduct(product.getId());
            stockList.stream()
                    .filter(item -> item.productId().equals(product.getId()))
                    .findFirst()
                    .ifPresent(item -> inStock.set(item.stock() > 0));

        } catch (Exception e) {
            log.warn("Inventory fetch failed", e);
        }
        return inStock.get();
    }
}
