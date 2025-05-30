package ru.sportmasterlab.Generate_order.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.sm.qaa.soap.gen.ComProOGate.CreateOrderResponse;
import ru.sm.qaa.soap.gen.MarsGate.SubmitByLinesResponse;
import ru.sportmasterlab.Generate_order.core.integration.CreateOrderInComPro;
import ru.sportmasterlab.Generate_order.core.integration.CreateOrderInMars;
import ru.sportmasterlab.Generate_order.model.Created.OrderRequest;
import ru.sportmasterlab.Generate_order.model.OrderDto;
import ru.sportmasterlab.Generate_order.repository.OrderRepository;

@Primary
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDto getorder(String orderCode) {
        return orderRepository.getOrderByCode(orderCode)
                .orElseThrow(() -> new OrderNotFoundException(orderCode));
    }

    @Override
    public void createOrder(String orderCode, String shopNum, String idWare, String price) {
        SubmitByLinesResponse submitResponse = CreateOrderInMars.createOrderInMars(shopNum, idWare, price);
        orderRepository.insertOrder(orderCode, "NO", "NO", "NO", "NO", "NO");
    }

    @Override
    public void createOrder(OrderRequest request) {
        CreateOrderResponse createOrderResponse;
        SubmitByLinesResponse submitResponse = CreateOrderInMars.createOrderInMars(
                request.shopNum(),
                request.itemList().get(0).idWare(),
                request.itemList().get(0).price());
        if (submitResponse.getResultCode() != -1) {
            orderRepository.insertOrder(request.orderCode(), "NO", "YES", "NO", "NO", "NO");
            createOrderResponse = CreateOrderInComPro.createOrderWareInComPro(
                    submitResponse,
                    request.shopNum(),
                    request.itemList().get(0).price());
            System.out.println(createOrderResponse.getResultText());
            Long orderCode = createOrderResponse.getOrderCode();
        }
    }

    @Override
    public void updateOrder(String orderCode, String orderId) {
        var order = orderRepository.getOrderByCode(orderId).orElseThrow(()->new OrderNotFoundException(orderId));
        orderRepository.updateOrder(orderCode,"YES", "YES", "YES", "YES", "YES");
    }
}
