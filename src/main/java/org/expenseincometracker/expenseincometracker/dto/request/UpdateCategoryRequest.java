package org.expenseincometracker.expenseincometracker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;


@Schema(description = "Request payload for updating a category")
public record UpdateCategoryRequest(

        @Schema(description = "Category update name", example = "Education")
        String name,

        @Schema(description = "updated monthly limit", example = "200.00")
        @Positive(message = "Monthly limit must be positive")
        BigDecimal monthlyLimit

) {}
