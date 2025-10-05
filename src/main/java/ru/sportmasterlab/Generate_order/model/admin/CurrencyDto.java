package ru.sportmasterlab.Generate_order.model.admin;

public record CurrencyDto(
        Long id,
        int currencyCode,
        String currencyType
) {
}