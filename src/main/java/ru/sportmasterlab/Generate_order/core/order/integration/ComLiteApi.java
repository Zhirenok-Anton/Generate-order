package ru.sportmasterlab.Generate_order.core.order.integration;

import ru.sm.qaa.soap.gen.ComProLite.FindClientOrderRequest;
import ru.sm.qaa.soap.gen.ComProLite.FindClientOrderResponse;
import ru.sm.qaa.soap.gen.ComProOGate.CreateOrderResponse;
import ru.sportmasterlab.Generate_order.model.order.created.OrderRequestDto;

import java.math.BigInteger;

public class ComLiteApi extends CreateOrderBase {

    public static FindClientOrderResponse getFindClientOrderResponse(OrderRequestDto request, CreateOrderResponse createOrderResponse){
        FindClientOrderRequest findClientOrderRequest = createFindClientOrderRequest(request,createOrderResponse);
        return comLiteApiPortType.findClientOrder(findClientOrderRequest);
    }

    private static FindClientOrderRequest createFindClientOrderRequest(OrderRequestDto request, CreateOrderResponse createOrderResponse){
        FindClientOrderRequest findClientOrderRequest = new FindClientOrderRequest();
        findClientOrderRequest.setOrderCode(BigInteger.valueOf(createOrderResponse.getOrderCode()));
        findClientOrderRequest.setShopNum(new BigInteger(request.shopNum()));
        return findClientOrderRequest;
    }
}
