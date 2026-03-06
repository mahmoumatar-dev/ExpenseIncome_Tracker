package org.expenseincometracker.expenseincometracker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public record CreateCategoryRequest(
        @NotBlank(message = "Category name is required")
        @Schema(description = "Name of the category", example = "Freelance")
        String name
) {
}
