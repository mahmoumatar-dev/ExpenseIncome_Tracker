package org.expenseincometracker.expenseincometracker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.expenseincometracker.expenseincometracker.enums.Currency;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Request payload for creating a new wallet")
public record CreateWalletRequest(

        @NotBlank(message = "Wallet name is required")
        @Schema(description = "Name of the wallet", example = "Family Wallet")
        String name,

        @NotNull(message = "Currency is required")
        @Schema(description = "Wallet currency", example = "USD")
        Currency currency,

        @NotNull(message = "Balance is required")
        @Schema(description = "Wallet Balance", example = "20000")
        BigDecimal balance,

        @Schema(description = "Optional list of child user IDs to assign to the wallet", example = "[5, 6]")
        List<Long> childrenIds
) {
}
