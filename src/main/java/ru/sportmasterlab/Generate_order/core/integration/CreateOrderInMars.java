package ru.sportmasterlab.Generate_order.core.integration;

import ru.sm.qaa.soap.gen.MarsGate.SubmitByLinesResponse;

import ru.sm.qaa.soap.gen.MarsGate.*;
import ru.sportmasterlab.Generate_order.model.Created.OrderRequest;

import static ru.sportmasterlab.Generate_order.core.integration.CreateOrderBase.*;

public class CreateOrderInMars extends CreateOrderBase{

    public static SubmitByLinesResponse createOrderInMars(OrderRequest request) {
        setDefaultValue();
        int lineMark = 1;
        SubmitByLinesRequest submitRequest = new SubmitByLinesRequest();
        TOrder tGateOrder = new TOrder();
        tGateOrder.setGateOrderGuid("10da5ac4-87ce-4adb-8985-03baec16b9a4");
        tGateOrder.setOrderTypeMnemocode("internal_pickup");

        TOrderItemLineList tGateOrderItemList = new TOrderItemLineList();
        for (int i =0;i<request.itemList().size();i++){
            for (int j = 0; j < Integer.parseInt(request.itemList().get(i).qtyOrdered()); j++){
                TOrderItemLine tGateOrderItem = new TOrderItemLine();
                tGateOrderItem.setPrice(Double.parseDouble(request.itemList().get(i).price()));
                tGateOrderItem.setIdWare(Long.valueOf(request.itemList().get(i).idWare()));
                tGateOrderItem.setLineMark(lineMark++);
                tGateOrderItem.setIsService((byte) 0);
                tGateOrderItemList.getOrderItem().add(i, tGateOrderItem);
            }
        }

        TOrderPickupParam tGateOrderPickupParam = new TOrderPickupParam();
        tGateOrderPickupParam.setShopNum(Long.parseLong(request.shopNum()));
        tGateOrder.setPickupParam(tGateOrderPickupParam);
        tGateOrder.setOrderItems(tGateOrderItemList);

        TOrderList tGateOrderList = new TOrderList();
        tGateOrderList.getOrder().add(tGateOrder);

        submitRequest.setOrders(tGateOrderList);
        submitRequest.setIdGateEntryPoint(IdGateEntryPoint);

        return marsGateApiPortType.submitByLines(submitRequest);
    }
}


