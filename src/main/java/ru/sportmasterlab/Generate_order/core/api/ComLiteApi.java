package ru.sportmasterlab.Generate_order.core.api;

import ru.sm.qaa.soap.gen.ComProLite.FindClientOrderRequest;
import ru.sm.qaa.soap.gen.ComProLite.FindClientOrderResponse;
import ru.sm.qaa.soap.gen.ComProOGate.CreateOrderResponse;
import ru.sportmasterlab.Generate_order.model.order.created.OrderRequest;

import java.math.BigInteger;

public class ComLiteApi extends CreateOrderBase {

    public static FindClientOrderResponse getFindClientOrderResponse(OrderRequest request, CreateOrderResponse createOrderResponse){
        FindClientOrderRequest findClientOrderRequest = createFindClientOrderRequest(request,createOrderResponse);
        return comLiteApiPortType.findClientOrder(findClientOrderRequest);
    }

    private static FindClientOrderRequest createFindClientOrderRequest(OrderRequest request, CreateOrderResponse createOrderResponse){
        FindClientOrderRequest findClientOrderRequest = new FindClientOrderRequest();
        findClientOrderRequest.setOrderCode(BigInteger.valueOf(createOrderResponse.getOrderCode()));
        findClientOrderRequest.setShopNum(new BigInteger(request.shopNum()));
        return findClientOrderRequest;
    }
}
