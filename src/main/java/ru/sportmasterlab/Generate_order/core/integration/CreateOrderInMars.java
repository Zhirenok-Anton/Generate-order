package ru.sportmasterlab.Generate_order.core.integration;

import ru.sm.qaa.soap.gen.MarsGate.SubmitByLinesResponse;

import ru.sm.qaa.soap.gen.MarsGate.*;

import static ru.sportmasterlab.Generate_order.core.integration.CreateOrderBase.*;

public class CreateOrderInMars {

    public static SubmitByLinesResponse createOrderInMars(String shopNum, String idWare, String price) {
        setTodayDate();
        SubmitByLinesRequest submitRequest = new SubmitByLinesRequest();
        TOrder tGateOrder = new TOrder();
        tGateOrder.setGateOrderGuid("10da5ac4-87ce-4adb-8985-03baec16b9a4");
        tGateOrder.setOrderTypeMnemocode("internal_pickup");

        TOrderItemLineList tGateOrderItemList = new TOrderItemLineList();

            TOrderItemLine tGateOrderItem = new TOrderItemLine();
            tGateOrderItem.setPrice(Double.parseDouble(price));
            tGateOrderItem.setIdWare(Long.valueOf(idWare));
            tGateOrderItem.setLineMark(1);
            tGateOrderItem.setIsService((byte) 0);
            tGateOrderItemList.getOrderItem().add(0, tGateOrderItem);


        TOrderPickupParam tGateOrderPickupParam = new TOrderPickupParam();
        tGateOrderPickupParam.setShopNum(Long.parseLong(shopNum));
        tGateOrder.setPickupParam(tGateOrderPickupParam);
        tGateOrder.setOrderItems(tGateOrderItemList);

        TOrderList tGateOrderList = new TOrderList();
        tGateOrderList.getOrder().add(tGateOrder);

        submitRequest.setOrders(tGateOrderList);
        submitRequest.setIdGateEntryPoint(IdGateEntryPoint);

        return marsGateApiPortType.submitByLines(submitRequest);
    }
}
/*
    //@Step("Создание заказа в МАРС с товарами с определенными МЦР и ценой")
    public static SubmitByLinesResponse createOrderInMars(int quantity, double price, Long... idMerchColorSize) {
        setTodayDate();
        SubmitByLinesRequest submitRequest = new SubmitByLinesRequest();
        TOrder tGateOrder = new TOrder();
        tGateOrder.setGateOrderGuid("10da5ac4-87ce-4adb-8985-03baec16b9a4");
        tGateOrder.setOrderTypeMnemocode("internal_pickup");

        TOrderItemLineList tGateOrderItemList = new TOrderItemLineList();
        for (int i = 0; i < quantity; i++) {
            TOrderItemLine tGateOrderItem = new TOrderItemLine();
            tGateOrderItem.setPrice(price);
            tGateOrderItem.setIdMerchColorSize(idMerchColorSize[i]);
            tGateOrderItem.setLineMark(i + 1);
            tGateOrderItem.setIsService((byte) 0);
            tGateOrderItemList.getOrderItem().add(i, tGateOrderItem);
        }

        TOrderPickupParam tGateOrderPickupParam = new TOrderPickupParam();
        tGateOrderPickupParam.setShopNum(Long.parseLong(cfg.ShopID()));
        tGateOrder.setPickupParam(tGateOrderPickupParam);
        tGateOrder.setOrderItems(tGateOrderItemList);

        TOrderList tGateOrderList = new TOrderList();
        tGateOrderList.getOrder().add(tGateOrder);


        submitRequest.setOrders(tGateOrderList);
        submitRequest.setIdGateEntryPoint(IdGateEntryPoint);

        return marsGateApiPortType.submitByLines(submitRequest);
    }

    //@Step("Создание заказа в МАРС с кредитом в оплате")
    public static SubmitByLinesResponse createOrderInMarsWithCreditPayment(int quantity) {
        setTodayDate();
        SubmitByLinesRequest submitRequest = new SubmitByLinesRequest();
        TOrder tGateOrder = new TOrder();
        tGateOrder.setGateOrderGuid("10da5ac4-87ce-4adb-8985-03baec16b9a4");

        tGateOrder.setOrderTypeMnemocode("internal_pickup");
        tGateOrder.setIdGtPaymentType(TINKOFF.getValue());
        TOrderItemLineList tGateOrderItemList = new TOrderItemLineList();
        for (int i = 0; i < quantity; i++) {
            TOrderItemLine tGateOrderItem = new TOrderItemLine();
            tGateOrderItem.setPrice(priceWare.doubleValue());
            tGateOrderItem.setIdWare(idWare);
            tGateOrderItem.setLineMark(i + 1);
            tGateOrderItem.setIsService((byte) 0);
            tGateOrderItemList.getOrderItem().add(i, tGateOrderItem);
        }
        TOrderPickupParam tGateOrderPickupParam = new TOrderPickupParam();
        tGateOrderPickupParam.setShopNum(Long.parseLong(cfg.ShopID()));
        tGateOrder.setPickupParam(tGateOrderPickupParam);
        tGateOrder.setOrderItems(tGateOrderItemList);

        TOrderList tGateOrderList = new TOrderList();
        tGateOrderList.getOrder().add(tGateOrder);

        submitRequest.setOrders(tGateOrderList);
        submitRequest.setIdGateEntryPoint(IdGateEntryPoint);

        return marsGateApiPortType.submitByLines(submitRequest);
    }
*/


