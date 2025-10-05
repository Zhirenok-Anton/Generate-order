package ru.sportmasterlab.Generate_order.core.order.integration;

import ru.sm.qaa.soap.gen.MarsGate.SubmitByLinesResponse;

import ru.sm.qaa.soap.gen.MarsGate.*;
import ru.sportmasterlab.Generate_order.model.order.created.ItemList;
import ru.sportmasterlab.Generate_order.model.order.created.OrderRequestDto;

public class MarsGateApi extends CreateOrderBase{

    public static SubmitByLinesResponse createOrderInMars(OrderRequestDto request) {
        int lineMark = 1;
        SubmitByLinesRequest submitRequest = new SubmitByLinesRequest();
        TOrder tGateOrder = new TOrder();
        tGateOrder.setGateOrderGuid("10da5ac4-87ce-4adb-8985-03baec16b9a4");
        tGateOrder.setOrderTypeMnemocode("internal_pickup");

        TOrderItemLineList tGateOrderItemList = new TOrderItemLineList();

        for (ItemList item :request.itemList()){
            for (int j = 0; j < Integer.parseInt(item.qtyOrdered()); j++){
                TOrderItemLine tGateOrderItem = new TOrderItemLine();
                tGateOrderItem.setPrice(Double.parseDouble(item.price()));
                tGateOrderItem.setIdWare(Long.valueOf(item.idWare()));
                tGateOrderItem.setLineMark(lineMark++);
                tGateOrderItem.setIsService((byte) 0);
                tGateOrderItemList.getOrderItem().add(tGateOrderItem);
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


