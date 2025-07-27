package ru.sportmasterlab.Generate_order.model.order;

public record OrderDto(
        long orderCode,
        String orderNum,
        int authCode

) {
}
