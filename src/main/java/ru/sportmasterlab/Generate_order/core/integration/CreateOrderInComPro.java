package ru.sportmasterlab.Generate_order.core.integration;

import ru.sm.qaa.soap.gen.ComProCsm.GetLogisticRequest;
import ru.sm.qaa.soap.gen.ComProCsm.LILogisticDocList;
import ru.sm.qaa.soap.gen.ComProCsm.LILogisticInfo;
import ru.sm.qaa.soap.gen.ComProLite.FindClientOrderRequest;
import ru.sm.qaa.soap.gen.ComProLite.FindClientOrderResponse;
import ru.sm.qaa.soap.gen.ComProOGate.*;
import ru.sm.qaa.soap.gen.MarsGate.SubmitByLinesResponse;
import ru.sportmasterlab.Generate_order.model.Created.OrderRequest;

import java.math.BigDecimal;

public class CreateOrderInComPro extends CreateOrderBase {

    public static final String EXECUTE_COM_PRO_RESERVE_CONSIGNEMNT = "CALL COM.COM_UI_ORDER_API.RESERVE_CONSIGNEMNT(?, 1)";
    public static final String EXECUTE_COM_PRO_CONFIRM_CONSIGNMENT = "CALL COM.COM_UI_ORDER_API.CONFIRM_CONSIGNMENT(?)";

    public static CreateOrderResponse createOrderWareInComPro(OrderRequest request, SubmitByLinesResponse submitResponse) {
        setOrderNum(submitResponse.getCalculations().getCalcSubmit().getFirst().getOrderNum());
        CreateOrderRequest createOrderRequest =
                createComproOrderRequest(request,submitResponse);
        return comProOGateApiPortType.createOrder(createOrderRequest);
    }

    public static void createGetLogistic(Long orderCode){
        BigDecimal consignmentCode = getLogistic(orderCode).getConsignmentList().getConsignment().getFirst().getCode();
        setStatusReserve(orderCode, consignmentCode);
    }

    private static CreateOrderRequest createComproOrderRequest(OrderRequest request,SubmitByLinesResponse submitResponse) {
        setTodayDate();
        setSumToPayWare(request);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();

        COMTOGTCOORDER comtogtcoOrder = new COMTOGTCOORDER();
        comtogtcoOrder.setOrderNum(orderNum);
        comtogtcoOrder.setOrderDate(TodayDay);
        comtogtcoOrder.setSourceId(15200299L);
        comtogtcoOrder.setEsmSessionId("23532-23434-2343434");
        comtogtcoOrder.setBasketNum(orderNum);

        COMTOGTCOMARS comtogtcomars = new COMTOGTCOMARS();
        comtogtcomars.setCalcCode(submitResponse.getCalculations().getCalcSubmit().getFirst().getMarsCalcCode());
        comtogtcomars.setNodeCode(submitResponse.getCalculations().getCalcSubmit().getFirst().getMarsNodeCode());

        comtogtcoOrder.setMars(comtogtcomars);
        comtogtcoOrder.setSourceTzh(3);
        comtogtcoOrder.setSourceTzm(0);

        COMTOGTCOMONEY comtogtcomoney = new COMTOGTCOMONEY();
        comtogtcomoney.setCurrencyCode(request.money().currencyCode());
        comtogtcomoney.setPrepay(BigDecimal.valueOf(0));//хардкод
        comtogtcomoney.setToPay(sumToPayWare);
        comtogtcomoney.setDiscountTotal(BigDecimal.valueOf(0.0));//хардкод
        comtogtcomoney.setBonusTotal(BigDecimal.valueOf(0.0));//хардкод
        comtogtcomoney.setPayType(1);//хардкод
        comtogtcomoney.setPaymentTypeId(10210299L);//хардкод
        /*if (isCredit) {
            comtogtcomoney.setPaymentTypeId(TINKOFF.getValue());
            comtogtcoOrder.setCreditProductId(11610299L);
        } else {
            comtogtcomoney.setPaymentTypeId(10210299L);
        }*/

        comtogtcoOrder.setMoney(comtogtcomoney);
        comtogtcoOrder.setMacrocity(11080299L);
        comtogtcoOrder.setClubCardOwnerRegCode(100000000627266576L);//хардкод
        comtogtcoOrder.setUseBonus(true);//хардкод
        comtogtcoOrder.setBonusReserveNum(orderNum);

        COMTOGTCOOWNER comtogtcoowner = new COMTOGTCOOWNER();
        comtogtcoowner.setId("100000000627266576");//хардкод
        comtogtcoOrder.setOwner(comtogtcoowner);

        //добавляем контактное лицо
        COMTOGTCOCONTACTPERSON comtogtcocontactperson = new COMTOGTCOCONTACTPERSON();
        comtogtcocontactperson.setName("qa");//хардкод
        comtogtcocontactperson.setPhone("79998887766");//хардкод
        comtogtcocontactperson.setEmail("test@test.ru");//хардкод
        comtogtcocontactperson.setPhoneConfirmed(true);//хардкод
        comtogtcocontactperson.setDoNotDisturb(true);//хардкод

        comtogtcoOrder.setContactPerson(comtogtcocontactperson);
        comtogtcoOrder.setReceivingMethodId(10050299);//хардкод
        //добавляем точку самовывоза
        COMTOGTCOPICKUP comtogtcopickup = new COMTOGTCOPICKUP();
        comtogtcopickup.setShopNum(Long.parseLong(request.shopNum()));
        comtogtcoOrder.setPickup(comtogtcopickup);
        //добавляем купоны
        /*if (coupons != null) {
            COMTOGTCOCOUPONT couponList = new COMTOGTCOCOUPONT();
            for (CouponListItemDto couponItem : coupons) {
                COMTOGTCOCOUPON coupon = new COMTOGTCOCOUPON();
                coupon.setNum(couponItem.getCoupon());
                coupon.setIsUsed(couponItem.getIsUsed());
                couponList.getCoupon().add(coupon);
            }
            comtogtcoOrder.setCouponList(couponList);
        }*/

        //добавляем товары
        COMTOGTCOLINET comtogtcolinetList = new COMTOGTCOLINET();
        for (int i = 0; i < request.itemList().size(); i++) {
            COMTOGTCOLINE comtogtcoline = new COMTOGTCOLINE();
            comtogtcoline.setIdWare(Long.valueOf(request.itemList().get(i).idWare()));
            comtogtcoline.setWareMark((long) i + 100);
            comtogtcoline.setQtyOrdered(Integer.parseInt(request.itemList().get(i).qtyOrdered()));
            comtogtcoline.setCatalogPrice(new BigDecimal(request.itemList().get(i).price()));
            comtogtcoline.setPrice(new BigDecimal(request.itemList().get(i).price()));
            comtogtcoline.setToPay(new BigDecimal(request.itemList().get(i).price()));
            comtogtcoline.setDiscountTotal(BigDecimal.valueOf(0.0));
            comtogtcolinetList.getLine().add(comtogtcoline);
        }
        comtogtcoOrder.setLineList(comtogtcolinetList);
        comtogtcoOrder.setEntryPoint(10140299L);
        //добавляем запрещенные акции
        /*if (forbidPromo != null) {
            COMTOGTCOFORBIDPROMOT forbidPromoList = new COMTOGTCOFORBIDPROMOT();
            for (ForbidPromoListDto promo : forbidPromo) {
                COMTOGTCOFORBIDPROMO fPromo = new COMTOGTCOFORBIDPROMO();
                fPromo.setId(promo.getCodePromo());
                fPromo.setName(promo.getNamePromo());
                forbidPromoList.getForbidPromo().add(fPromo);
            }
            comtogtcoOrder.setForbidPromoList(forbidPromoList);
        }*/

        createOrderRequest.setOrder(comtogtcoOrder);
        return createOrderRequest;
    }

    public static LILogisticInfo getLogistic(Long orderCode) {
        GetLogisticRequest getLogisticRequest = new GetLogisticRequest();
        getLogisticRequest.setOrderCode(BigDecimal.valueOf(orderCode));
        return comCsmApiPortType.getLogistic(getLogisticRequest).getLogisticInfo();
    }

    public static void setStatusReserve(Long orderCode, BigDecimal consignmentCode) {
        OracleDBService jdbi = new OracleDBService();
        LILogisticInfo liLogisticInfo;
        BigDecimal logisiticDocState;
        int logisiticDocStateInt = 1;

        while (logisiticDocStateInt == 1) {
            jdbi.oneExecute(EXECUTE_COM_PRO_RESERVE_CONSIGNEMNT, consignmentCode);
            liLogisticInfo = getLogistic(orderCode);
            LILogisticDocList logisticDocList = liLogisticInfo.getConsignmentList().getConsignment().getFirst()
                    .getLogisticDocList();
            if (logisticDocList != null) {
                logisiticDocState = logisticDocList.getLogisticDocLine().getFirst()
                        .getState();
                logisiticDocStateInt = Integer.parseInt(logisiticDocState.toString());
            }
        }
        if (logisiticDocStateInt == 3) {
            jdbi.oneExecute(EXECUTE_COM_PRO_CONFIRM_CONSIGNMENT, consignmentCode);
        }
    }
}
