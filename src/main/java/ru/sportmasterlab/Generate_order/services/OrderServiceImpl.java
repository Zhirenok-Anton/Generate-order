package ru.sportmasterlab.Generate_order.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.sm.qaa.soap.gen.ComProOGate.CreateOrderResponse;
import ru.sm.qaa.soap.gen.ComProPGate.CreatePaymentResponse;
import ru.sm.qaa.soap.gen.MarsGate.SubmitByLinesResponse;
import ru.sportmasterlab.Generate_order.core.integration.CreateOrderInComPro;
import ru.sportmasterlab.Generate_order.core.integration.CreateOrderInMars;
import ru.sportmasterlab.Generate_order.model.Created.OrderRequest;
import ru.sportmasterlab.Generate_order.model.OrderDto;
import ru.sportmasterlab.Generate_order.repository.OrderRepository;

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
    public void createOrder(Long orderCode, String shopNum, String idWare, String price) {
        SubmitByLinesResponse submitResponse = CreateOrderInMars.createOrderInMars(shopNum, idWare, price);
        orderRepository.insertOrder(orderCode, "NO", "NO", "NO", "NO", "NO");
    }

    @Override
    public Long createOrder(OrderRequest request) {
        CreateOrderResponse createOrderResponse = null;
        SubmitByLinesResponse submitResponse;
        CreatePaymentResponse createPaymentResponse = null;
        Long orderCode = 0L;

        submitResponse = CreateOrderInMars.createOrderInMars(
                request.shopNum(),
                request.itemList().get(0).idWare(),
                request.itemList().get(0).price());
        if (submitResponse.getResultCode() != -1) {
            createOrderResponse = CreateOrderInComPro.createOrderWareInComPro(
                    submitResponse,
                    request.shopNum(),
                    request.itemList().get(0).price());
            orderCode = createOrderResponse.getOrderCode();
        }
        if (orderCode != 0L) {
            createPaymentResponse = createPaymentResponseByType(
                    request.shopNum(),
                    submitResponse,
                    createOrderResponse,
                    setBankCardPayment(
                            request.itemList().size(),
                            Double.valueOf(request.itemList().get(0).price())),
                    request.itemList().size(),
                    Double.valueOf(request.itemList().get(0).price()));
        }
        orderRepository.insertOrder(orderCode, "NO", "YES", "NO", "NO", "NO");

        return orderCode;
    }

    @Override
    public void updateOrder(Long orderCode, String orderId) {

    }

}
