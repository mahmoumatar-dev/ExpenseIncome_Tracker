package org.expenseincometracker.expenseincometracker.dto.response;

public record UserDistributionResponse(
        Long parentCount,
        Long childCount,
        Double parentPercentage,
        Double childPercentage
) {
}
