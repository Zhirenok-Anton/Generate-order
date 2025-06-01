package ru.sportmasterlab.Generate_order.model;

public record OrderDto(
        long orderCode,
        String orderNum,
        String csmStatus,
        String marsStatus,
        String comproStatus,
        String kisrmStatus,
        String order
) {
}
