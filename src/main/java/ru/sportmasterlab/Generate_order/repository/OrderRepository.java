package ru.sportmasterlab.Generate_order.repository;

import ru.sportmasterlab.Generate_order.model.order.OrderDto;


import java.util.Optional;

public interface OrderRepository {

    Optional<OrderDto> getOrderByCode(Long orderCode);
    void insertOrder(Long orderCode,String orderNum, int authCode, String csmStatus, String marsStatus, String comproStatus, String kisrmStatus, String order);
}
