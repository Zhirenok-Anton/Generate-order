package ru.sportmasterlab.Generate_order.services;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.sm.qaa.soap.gen.ComProOGate.CreateOrderResponse;
import ru.sm.qaa.soap.gen.ComProPGate.CreatePaymentResponse;
import ru.sm.qaa.soap.gen.MarsGate.SubmitByLinesResponse;
import ru.sportmasterlab.Generate_order.core.integration.CreateOrderInComPro;
import ru.sportmasterlab.Generate_order.core.integration.CreateOrderInMars;
import ru.sportmasterlab.Generate_order.core.integration.OracleDBService;
import ru.sportmasterlab.Generate_order.model.Created.OrderRequest;
import ru.sportmasterlab.Generate_order.model.OrderDto;
import ru.sportmasterlab.Generate_order.repository.OrderRepository;

import java.math.BigDecimal;

import static ru.sportmasterlab.Generate_order.core.integration.CreateOrderInComPro.*;
import static ru.sportmasterlab.Generate_order.core.integration.CreatePayment.*;

@Primary
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDto getorder(Long orderCode) {
        return orderRepository.getOrderByCode(orderCode)
                .orElseThrow(() -> new OrderNotFoundException(orderCode));
    }

    @Override
    public Long createOrder(OrderRequest request) {
        CreateOrderResponse createOrderResponse = null;
        SubmitByLinesResponse submitResponse= null;
        CreatePaymentResponse createPaymentResponse = null;
        Long orderCode = 0L;

        //вызов MARS для получения номера заказа. Работает плохо! придется просить коллег заводить товары в своей системе
        //придумать как можно обоуйтись без MARS
        // от MARS нужно 3 значения: MarsCalcCode, - счетчик внутри марса (14431600)
        //                           MarsNodeCode - статичное значение  = 100
        //                           OrderNum - это можно генерировать
        submitResponse = CreateOrderInMars.createOrderInMars(request);

        //вызов ComPro - для создания заказа
        createOrderResponse = CreateOrderInComPro.createOrderWareInComPro(request,submitResponse);
        orderCode = createOrderResponse.getOrderCode();

        //создание платежа
        createPaymentResponse = createPaymentResponseByType(
                request,
                createOrderResponse,
                setBankCardPayment(
                        request.itemList().size(),
                        Double.valueOf(request.itemList().get(0).price())));

        //резервирование заказа в компро
        setStatusReserve(orderCode, getLogistic(orderCode).getConsignmentList().getConsignment().getFirst().getCode());

        orderRepository.insertOrder(orderCode,submitResponse.getCalculations().getCalcSubmit().getFirst().getOrderNum(), "YES", "YES", "YES", "NO", "{123,123,123}");

        return orderCode;
    }

    public static void main(String[] args){

    }

    @Override
    public void updateOrder(Long orderCode, String orderId) {

    }
}
