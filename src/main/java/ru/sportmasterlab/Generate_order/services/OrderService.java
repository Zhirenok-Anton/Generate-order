package ru.sportmasterlab.Generate_order.services;

import ru.sportmasterlab.Generate_order.model.Created.OrderRequest;
import ru.sportmasterlab.Generate_order.model.OrderDto;

public interface OrderService {

    OrderDto getorder(String orderCode);
    void createOrder(String orderCode, String shopNum, String idWare, String price);
    void createOrder(OrderRequest request);
    void updateOrder(String orderCode, String orderId);

}

