package ru.sportmasterlab.Generate_order.model.order;

public record OrderResponseDto(
        long orderCode,
        String orderNum,
        int authCode

) {
}
