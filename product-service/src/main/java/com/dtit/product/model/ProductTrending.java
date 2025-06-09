package com.dtit.product.model;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash("ProductTrending")
public class ProductTrending implements Serializable {

    private String id;
    private String name;
    private String image;
    private double price;

}
