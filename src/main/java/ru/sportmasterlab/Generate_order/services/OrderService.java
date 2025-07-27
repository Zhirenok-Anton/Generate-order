package ru.sportmasterlab.Generate_order.services;

import ru.sportmasterlab.Generate_order.model.order.created.OrderRequest;
import ru.sportmasterlab.Generate_order.model.order.OrderDto;

public interface OrderService {

    OrderDto getorder(Long orderCode);
    Long createOrder(OrderRequest request);
}

