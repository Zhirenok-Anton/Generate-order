package ru.sportmasterlab.Generate_order.repository;

import ru.sportmasterlab.Generate_order.model.order.OrderResponseDto;

import java.util.Optional;

public interface OrderRepository {

    Optional<OrderResponseDto> getOrderByCode(Long orderCode);
    void insertOrder(Long orderCode,String orderNum, int authCode, String order);
}
