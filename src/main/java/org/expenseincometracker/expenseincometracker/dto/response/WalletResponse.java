package org.expenseincometracker.expenseincometracker.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.expenseincometracker.expenseincometracker.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Wallet response containing wallet details and assigned children")
public record WalletResponse(

        @Schema(description = "Wallet ID", example = "1")
        Long id,

        @Schema(description = "Wallet name", example = "Family Wallet")
        String name,

        @Schema(description = "Wallet currency", example = "USD")
        Currency currency,

        @Schema(description = "Wallet balance", example = "2000.00")
        BigDecimal balance,

        @Schema(description = "List of assigned children")
        List<ChildSummary> children,

        @Schema(description = "Wallet creation timestamp")
        LocalDateTime createdAt,

        @Schema(description = "Wallet last update timestamp")
        LocalDateTime updatedAt
) {

    @Schema(description = "Summary of an assigned child user")
    public record ChildSummary(

            @Schema(description = "Child user ID", example = "5")
            Long id,

            @Schema(description = "Child user name", example = "Ali")
            String name
    ) {
    }
}
