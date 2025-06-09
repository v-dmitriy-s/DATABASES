package com.dtit.inventory.repositories;

import com.dtit.inventory.model.InventoryItem;
import org.springframework.stereotype.Repository;

@Repository
public class InventoryRepository extends CrudRepository<InventoryItem, String, String> {

    public InventoryRepository() {
        super(InventoryItem.class);
    }

//    List<User> findByUserId(String userId);
}
