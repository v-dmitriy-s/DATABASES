package com.dtit.product.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "Variant entity representing a specific version of a product (e.g., size/color combination).")
public class Variant {
    @Schema(description = "Stock Keeping Unit (SKU) for the variant", example = "SKU-001-BLK-M", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1, maxLength = 50, pattern = "^[a-zA-Z0-9\\-]+$")
    private String sku; // Stock Keeping Unit

    @Schema(description = "Price for this variant (overrides base price)", example = "209.99", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private BigDecimal price; // Overrides basePrice

    @Schema(description = "Available stock quantity for this variant", example = "50", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private int stockQuantity;

    @Schema(description = "List of attribute name/value pairs defining this variant")
    private List<Attribute> attributeValues; // Embedded attribute-value pairs
} 