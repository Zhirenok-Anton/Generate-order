package ru.sportmasterlab.Generate_order.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.sm.qaa.soap.gen.ComProOGate.CreateOrderResponse;
import ru.sm.qaa.soap.gen.MarsGate.SubmitByLinesResponse;
import ru.sportmasterlab.Generate_order.core.integration.CreateOrderInComPro;
import ru.sportmasterlab.Generate_order.core.integration.CreateOrderInMars;
import ru.sportmasterlab.Generate_order.model.order.created.OrderRequest;
import ru.sportmasterlab.Generate_order.model.order.OrderDto;
import ru.sportmasterlab.Generate_order.repository.OrderRepository;

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
        CreateOrderResponse createOrderResponse= null;
        SubmitByLinesResponse submitResponse = null;
        Long orderCode = 0L;

        //TODO:вызов MARS для получения номера заказа. Работает плохо! придется просить коллег заводить товары в своей системе
        //придумать как можно обоуйтись без MARS
        // от MARS нужно 3 значения: MarsCalcCode, - счетчик внутри марса (пример 14431600)
        //                           MarsNodeCode - статичное значение  = 100
        //                           OrderNum - возможно это можно генерировать  самостоятельно (пример 123456-123456)
        submitResponse = CreateOrderInMars.createOrderInMars(request);

        //вызов ComPro - для создания заказа
        createOrderResponse = CreateOrderInComPro.createOrderWareInComPro(request,submitResponse);
        orderCode = createOrderResponse.getOrderCode();

        //создание платежа
        createPaymentResponseByType(request, createOrderResponse);

        //резервирование заказа в компро
        setStatusReserve(orderCode, getLogistic(orderCode).getConsignmentList().getConsignment().getFirst().getCode());
        int authCode = 0;
        if(!request.money().paymentType().equals("IN_SHOP")) {
            authCode = getComproOrder(request, createOrderResponse).getOrderList().getFindClientOrderItem().getFirst().getAuthCode();
        }
        orderRepository.insertOrder(
                orderCode,
                submitResponse.getCalculations().getCalcSubmit().getFirst().getOrderNum(),
                authCode,
                "YES", "YES", "YES", "NO", request.toString());
        return orderCode;
    }
}
