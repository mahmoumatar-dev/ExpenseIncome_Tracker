package org.expenseincometracker.expenseincometracker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.expenseincometracker.expenseincometracker.enums.TransactionType;

import java.math.BigDecimal;

public record CreateTransactionRequest(

        @NotNull(message = "Transaction type is required")
        @Schema(description = "Transaction type [EXPENSE - INCOME]", example = "EXPENSE")
        TransactionType type,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        @Schema(description = "Amount of the transaction", example = "200.00")
        BigDecimal amount,

        @NotNull(message = "Wallet ID is required")
        @Schema(description = "Wallet id that do the transaction", example = "1")
        Long walletId,

        @Schema(description = "Category id that of the expense transaction ", example = "1")
        Long categoryId,

        @Schema(description = "Description of transaction (optional)", example = "expense transaction description")
        String description
) {

}
