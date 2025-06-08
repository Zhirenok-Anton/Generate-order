package ru.sportmasterlab.Generate_order.services;

import ru.sportmasterlab.Generate_order.model.Created.OrderRequest;
import ru.sportmasterlab.Generate_order.model.OrderDto;

public interface OrderService {

    OrderDto getorder(Long orderCode);
    Long createOrder(OrderRequest request);
    void updateOrder(Long orderCode, String orderId);

}

