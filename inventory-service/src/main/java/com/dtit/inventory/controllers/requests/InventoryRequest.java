package com.dtit.inventory.controllers.requests;

public record InventoryRequest( String productId,
                                int stock) {
}
