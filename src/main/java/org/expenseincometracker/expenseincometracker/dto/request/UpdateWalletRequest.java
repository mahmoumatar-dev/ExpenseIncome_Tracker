package org.expenseincometracker.expenseincometracker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.expenseincometracker.expenseincometracker.enums.Currency;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Request payload for updating an existing wallet. All fields are optional for partial updates.")
public record UpdateWalletRequest(

        @Schema(description = "Updated wallet name", example = "Updated Wallet Name")
        String name,

        @Schema(description = "Updated wallet currency", example = "EUR")
        Currency currency,

        @Schema(description = "Wallet Balance", example = "20000")
        BigDecimal balance,

        @Schema(description = "Updated list of child user IDs to assign to the wallet", example = "[7, 8]")
        List<Long> childrenIds
) {
}
