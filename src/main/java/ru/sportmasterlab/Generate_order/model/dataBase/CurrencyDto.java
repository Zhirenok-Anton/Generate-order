package ru.sportmasterlab.Generate_order.model.dataBase;

public record CurrencyDto(
        Long id,
        int currencyCode,
        String currencyType
) {
}