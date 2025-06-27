package com.dtit.product.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Attribute entity representing a name/value pair for a product variant (e.g., Color: Red)")
public class Attribute {
    @Schema(description = "Name of the attribute", example = "Color", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1, maxLength = 30, pattern = "^[A-Za-z]+$")
    private String name; // e.g., Color, Size, Material

    @Schema(description = "Value of the attribute", example = "Red", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1, maxLength = 30, pattern = "^[A-Za-z0-9 ]+$")
    private String value; // e.g., Red, Large, Cotton
} 