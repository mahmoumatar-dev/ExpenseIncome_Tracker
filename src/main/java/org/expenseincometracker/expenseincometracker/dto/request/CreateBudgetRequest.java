package org.expenseincometracker.expenseincometracker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;


public record CreateBudgetRequest (
        @NotNull(message = "Category ID is required")
        @Schema(description = "Id of the category", example = "1")
        Long categoryId,

         @NotNull(message = "Monthly limit is required")
         @Positive(message = "Monthly limit must be positive")
         @Schema(description = "Monthly limit for budget", example = "500.00")
         BigDecimal monthlyLimit
){

}
