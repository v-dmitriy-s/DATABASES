package com.dtit.product.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "categories")
@Schema(description = "Category entity representing a product category, possibly with subcategories.")
public class Category {
    @Id
    @Schema(description = "Unique identifier for the category", example = "cat-001", requiredMode = Schema.RequiredMode.REQUIRED, pattern = "^[a-zA-Z0-9\\-]+$")
    private String categoryId;

    @Schema(description = "Name of the category", example = "Electronics", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 100)
    private String name;

    @Schema(description = "Description of the category", example = "All electronic devices and gadgets.")
    private String description;

    @Schema(description = "List of subcategories under this category")
    private List<Category> subCategories;
} 