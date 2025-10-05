package ru.sportmasterlab.Generate_order.services;

import ru.sportmasterlab.Generate_order.model.order.created.OrderRequestDto;
import ru.sportmasterlab.Generate_order.model.order.OrderResponseDto;

public interface OrderService {

    OrderResponseDto getorder(Long orderCode);
    Long createOrder(OrderRequestDto request);
}

