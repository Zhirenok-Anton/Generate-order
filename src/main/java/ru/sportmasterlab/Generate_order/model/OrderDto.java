package ru.sportmasterlab.Generate_order.model;

public record OrderDto(
        String orderCode,
        String csmStatus,
        String marsStatus,
        String comproStatus,
        String kisrmStatus,
        String order
) {
}
