package ru.sportmasterlab.Generate_order.repository;

import ru.sportmasterlab.Generate_order.model.OrderDto;


import java.util.Optional;

public interface OrderRepository {

    Optional<OrderDto> getOrderByCode(String orderCode);
    void insertOrder(String orderCode, String csmStatus, String marsStatus, String comproStatus, String kisrmStatus, String order);
    void updateOrder(String orderCode, String csmStatus, String marsStatus, String comproStatus, String kisrmStatus, String order);
    void deleteProfileById(String orderCode);
}
