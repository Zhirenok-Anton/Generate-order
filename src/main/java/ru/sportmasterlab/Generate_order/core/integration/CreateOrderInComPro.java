package ru.sportmasterlab.Generate_order.core.integration;

import ru.sm.qaa.soap.gen.ComProOGate.*;
import ru.sm.qaa.soap.gen.MarsGate.SubmitByLinesResponse;
import ru.sm.qaa.soap.gen.MarsGate.TCalcSubmit;

import java.math.BigDecimal;

public class CreateOrderInComPro extends CreateOrderBase {

    public static CreateOrderResponse createOrderWareInComPro(SubmitByLinesResponse submitResponse, String shopNum, String price) {
        CreateOrderRequest createOrderRequest =
                createComproOrderRequest(submitResponse,shopNum,price);

        return comProOGateApiPortType.createOrder(createOrderRequest);
    }

    private static CreateOrderRequest createComproOrderRequest(SubmitByLinesResponse submitResponse,String shopNum,String price) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        TCalcSubmit tCalcSubmit = submitResponse.getCalculations().getCalcSubmit().getFirst();

        COMTOGTCOORDER comtogtcoOrder = new COMTOGTCOORDER();
        comtogtcoOrder.setOrderNum(tCalcSubmit.getOrderNum());
        comtogtcoOrder.setOrderDate(TodayDay);
        comtogtcoOrder.setSourceId(15200299L);
        comtogtcoOrder.setEsmSessionId("23532-23434-2343434");
        comtogtcoOrder.setBasketNum(tCalcSubmit.getOrderNum());

        COMTOGTCOMARS comtogtcomars = new COMTOGTCOMARS();
        comtogtcomars.setCalcCode(tCalcSubmit.getMarsCalcCode());
        comtogtcomars.setNodeCode(tCalcSubmit.getMarsNodeCode());

        comtogtcoOrder.setMars(comtogtcomars);
        comtogtcoOrder.setSourceTzh(3);
        comtogtcoOrder.setSourceTzm(0);

        BigDecimal sumToPayWare = new BigDecimal(price);

        COMTOGTCOMONEY comtogtcomoney = new COMTOGTCOMONEY();
        comtogtcomoney.setCurrencyCode("643");
        comtogtcomoney.setPrepay(BigDecimal.valueOf(0));
        comtogtcomoney.setToPay(sumToPayWare);
        comtogtcomoney.setDiscountTotal(BigDecimal.valueOf(0.0));
        comtogtcomoney.setBonusTotal(BigDecimal.valueOf(0.0));
        comtogtcomoney.setPayType(1);
        comtogtcomoney.setPaymentTypeId(10210299L);
        /*if (isCredit) {
            comtogtcomoney.setPaymentTypeId(TINKOFF.getValue());
            comtogtcoOrder.setCreditProductId(11610299L);
        } else {
            comtogtcomoney.setPaymentTypeId(10210299L);
        }*/

        comtogtcoOrder.setMoney(comtogtcomoney);
        comtogtcoOrder.setMacrocity(11080299L);
        comtogtcoOrder.setClubCardOwnerRegCode(100000000627266576L);
        comtogtcoOrder.setUseBonus(true);
        comtogtcoOrder.setBonusReserveNum(tCalcSubmit.getOrderNum());

        COMTOGTCOOWNER comtogtcoowner = new COMTOGTCOOWNER();
        comtogtcoowner.setId("100000000627266576");
        comtogtcoOrder.setOwner(comtogtcoowner);
        //добавляем контактное лицо
        COMTOGTCOCONTACTPERSON comtogtcocontactperson = new COMTOGTCOCONTACTPERSON();
        comtogtcocontactperson.setName("qa");
        comtogtcocontactperson.setPhone("79998887766");
        comtogtcocontactperson.setEmail("test@test.ru");
        comtogtcocontactperson.setPhoneConfirmed(true);
        comtogtcocontactperson.setDoNotDisturb(true);

        comtogtcoOrder.setContactPerson(comtogtcocontactperson);
        comtogtcoOrder.setReceivingMethodId(10050299);
        //добавляем точку самовывоза
        COMTOGTCOPICKUP comtogtcopickup = new COMTOGTCOPICKUP();
        comtogtcopickup.setShopNum(Long.parseLong(shopNum));
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
        for (int i = 0; i < 1; i++) {
            COMTOGTCOLINE comtogtcoline = new COMTOGTCOLINE();
            comtogtcoline.setIdWare(idWare);
            comtogtcoline.setWareMark((long) i + 100);
            comtogtcoline.setQtyOrdered(1);
            comtogtcoline.setCatalogPrice(new BigDecimal(price));
            comtogtcoline.setPrice(new BigDecimal(price));
            comtogtcoline.setToPay(new BigDecimal(price));
            comtogtcoline.setDiscountTotal(BigDecimal.valueOf(0.0));
            comtogtcolinetList.getLine().add(comtogtcoline);
            sizeWare = i;
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
}
