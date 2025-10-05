package ru.sportmasterlab.Generate_order.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.sm.qaa.soap.gen.ComProOGate.CreateOrderResponse;
import ru.sm.qaa.soap.gen.MarsGate.SubmitByLinesResponse;
import ru.sportmasterlab.Generate_order.core.order.integration.ComProOGateApi;
import ru.sportmasterlab.Generate_order.core.order.integration.MarsGateApi;
import ru.sportmasterlab.Generate_order.model.order.created.OrderRequestDto;
import ru.sportmasterlab.Generate_order.model.order.OrderResponseDto;
import ru.sportmasterlab.Generate_order.repository.OrderRepository;

import static ru.sportmasterlab.Generate_order.core.order.integration.ComPgateApi.*;
import static ru.sportmasterlab.Generate_order.core.order.integration.ComLiteApi.*;
import static ru.sportmasterlab.Generate_order.core.order.integration.ComCsmApi.*;

@Primary
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponseDto getorder(Long orderCode) {
        return orderRepository.getOrderByCode(orderCode)
                .orElseThrow(() -> new OrderNotFoundException(orderCode));
    }

    @Override
    public Long createOrder(OrderRequestDto request) {
        CreateOrderResponse createOrderComProResponse= null;
        SubmitByLinesResponse submitResponse = null;
        Long orderCode = 0L;

        //TODO:вызов MARS для получения номера заказа. Работает плохо! придется просить коллег заводить товары в своей системе
        //придумать как можно обоуйтись без MARS
        // от MARS нужно 3 значения: MarsCalcCode, - счетчик внутри марса (пример 14431600)
        //                           MarsNodeCode - статичное значение  = 100
        //                           OrderNum - возможно это можно генерировать  самостоятельно (пример 123456-123456)
        submitResponse = MarsGateApi.createOrderInMars(request);

        //создания заказа
        createOrderComProResponse = ComProOGateApi.getCreateOrderComProResponse(request,submitResponse);
        orderCode = createOrderComProResponse.getOrderCode();

        //создание платежа
        getCreatePaymentResponse(request, createOrderComProResponse);

        //резервирование заказа в компро
        setStatusReserve(orderCode, getGetLogisticResponse(orderCode).getLogisticInfo().getConsignmentList().getConsignment().getFirst().getCode());
        int authCode = 0;
        if(!request.money().paymentType().equals("IN_SHOP")) {
            authCode = getFindClientOrderResponse(request, createOrderComProResponse).getOrderList().getFindClientOrderItem().getFirst().getAuthCode();
        }
        orderRepository.insertOrder(
                orderCode,
                submitResponse.getCalculations().getCalcSubmit().getFirst().getOrderNum(),
                authCode,
                request.toString());
        return orderCode;
    }
}