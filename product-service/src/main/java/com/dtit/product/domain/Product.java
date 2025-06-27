package com.dtit.product.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.List;

@Data
@Document(collection = "products")
@Schema(description = "Product entity representing a sellable item with variants.")
public class Product {
    @Id
    @Schema(description = "Unique identifier for the product", example = "12345", requiredMode = Schema.RequiredMode.REQUIRED, pattern = "^[a-zA-Z0-9\\-]+$")
    private String productId;

    @Schema(description = "Name of the category this product belongs to", example = "Electronics", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 100)
    private String categoryName;

    @Schema(description = "Name of the product", example = "Wireless Headphones", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 100)
    private String name;

    @Schema(description = "Detailed description of the product", example = "High-quality noise-canceling headphones.")
    private String description;

    @Schema(description = "Base price of the product", example = "199.99", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private BigDecimal basePrice;

    @Schema(description = "Whether the product is published and visible to shoppers", example = "true")
    private boolean isPublished;

    @Schema(description = "List of variants for this product")
    private List<Variant> variants;
}
