package com.dtit.inventory.controllers;

import com.dtit.inventory.controllers.requests.InventoryRequest;
import com.dtit.inventory.controllers.responses.InventoryResponse;
import com.dtit.inventory.model.InventoryItem;
import com.dtit.inventory.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("inventory")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/stocks")
    public List<InventoryResponse> getAllStocks() {
        return inventoryService.getAll().stream()
                .map(item -> new InventoryResponse(item.getProductId(), item.getStockCount()))
                .toList();
    }

    @GetMapping("/{productId}")
    public List<InventoryItem> getStockByProduct(@PathVariable String productId) {
        return inventoryService.getStockByProduct(productId);
    }

    @GetMapping("/{productId}/{location}")
    public InventoryItem getStockAtLocation(@PathVariable String productId, @PathVariable String location) {
        return inventoryService.getStockAtLocation(productId, location);
    }

//    @PostMapping
//    public InventoryItem createOrUpdateStock(@RequestBody InventoryItem item) {
//        return inventoryService.saveOrUpdate(item);
//    }

    @DeleteMapping("/{productId}/{location}")
    public void deleteStockEntry(@PathVariable String productId, @PathVariable String location) {
        inventoryService.deleteStock(productId, location);
    }

    @PostMapping
    public InventoryResponse createOrUpdate(@RequestBody InventoryRequest request) {
        InventoryItem item = new InventoryItem();
        item.setProductId(request.productId());
        item.setLocation("default"); // or extract from elsewhere
        item.setStockCount(request.stock());

        InventoryItem saved = inventoryService.saveOrUpdate(item);
        return new InventoryResponse(saved.getProductId(), saved.getStockCount());
    }
}
