package ru.sportmasterlab.Generate_order.model.order.created;

public record Money(
        String currencyCode,
        String paymentType
) {
}