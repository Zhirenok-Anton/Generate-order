package ru.sportmasterlab.Generate_order.core.api;

import ru.sm.qaa.soap.gen.ComProOGate.*;
import ru.sm.qaa.soap.gen.MarsGate.SubmitByLinesResponse;
import ru.sportmasterlab.Generate_order.model.order.created.OrderRequest;

import java.math.BigDecimal;

public class ComProOGateApi extends CreateOrderBase {

    public static CreateOrderResponse getCreateOrderComProResponse(OrderRequest request, SubmitByLinesResponse submitResponse) {
        setOrderNum(submitResponse.getCalculations().getCalcSubmit().getFirst().getOrderNum());
        CreateOrderRequest createOrderRequestComPro =
                createOrderRequest(request,submitResponse);
        return comProOGateApiPortType.createOrder(createOrderRequestComPro);
    }

    private static CreateOrderRequest createOrderRequest(OrderRequest request, SubmitByLinesResponse submitResponse) {
        setTodayDate();

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
        comtogtcomoney.setCurrencyCode(getCurrencyCode(request));
        comtogtcomoney.setPrepay(BigDecimal.valueOf(0));//хардкод
        comtogtcomoney.setToPay(getSumToPayWare(request));
        comtogtcomoney.setDiscountTotal(getDiscountTotal(request));
        comtogtcomoney.setBonusTotal(getBonusTotal());
        comtogtcomoney.setPayType(1);//хардкод
        comtogtcomoney.setPaymentTypeId(getPaymentTypeId(request));

        comtogtcoOrder.setMoney(comtogtcomoney);
        if (getCreditProductId(request)!= null) comtogtcoOrder.setCreditProductId(getCreditProductId(request));
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
        //int i = 0;
       /* for (ItemList item : request.itemList()) {

            COMTOGTCOLINE comtogtcoline = new COMTOGTCOLINE();
            comtogtcoline.setIdWare(Long.valueOf(item.idWare()));
            comtogtcoline.setWareMark((long) i+++100);
            comtogtcoline.setQtyOrdered(Integer.parseInt(item.qtyOrdered()));
            comtogtcoline.setCatalogPrice(new BigDecimal(item.price()));
            comtogtcoline.setPrice(new BigDecimal(item.price()).subtract(getDiscountTotalWare(item)));
            comtogtcoline.setToPay(new BigDecimal(item.price()).subtract(getDiscountTotalWare(item)));
            comtogtcoline.setDiscountTotal(getDiscountTotalWare(request.itemList().get(i)));
        }*/
        for (int i = 0; i < request.itemList().size(); i++) {
            COMTOGTCOLINE comtogtcoline = new COMTOGTCOLINE();
            comtogtcoline.setIdWare(Long.valueOf(request.itemList().get(i).idWare()));
            comtogtcoline.setWareMark((long) i + 100);
            comtogtcoline.setQtyOrdered(Integer.parseInt(request.itemList().get(i).qtyOrdered()));
            comtogtcoline.setCatalogPrice(new BigDecimal(request.itemList().get(i).price()));
            comtogtcoline.setPrice(new BigDecimal(request.itemList().get(i).price()).subtract(getDiscountTotalWare(request.itemList().get(i)).divide(BigDecimal.valueOf(Long.parseLong(request.itemList().get(i).qtyOrdered())))));// comtogtcoline.setPrice(new BigDecimal(request.itemList().get(i).price()).subtract(getDiscountTotalWare(request.itemList().get(i))));
            comtogtcoline.setToPay((new BigDecimal(request.itemList().get(i).price()).multiply(BigDecimal.valueOf(Long.parseLong(request.itemList().get(i).qtyOrdered())))).subtract(getDiscountTotalWare(request.itemList().get(i))));//comtogtcoline.setToPay(new BigDecimal(request.itemList().get(i).price()).subtract(getDiscountTotalWare(request.itemList().get(i))));
            comtogtcoline.setDiscountTotal(getDiscountTotalWare(request.itemList().get(i)));

            if (request.itemList().get(i).discountList()!=null) {
                COMTOGTCOAPPLIEDDISCOUNTT comtogtcoapplieddiscountList = new COMTOGTCOAPPLIEDDISCOUNTT();
                for (int j = 0; j < request.itemList().get(i).discountList().size(); j++) {
                    COMTOGTCOAPPLIEDDISCOUNT comtogtcoapplieddiscountLine = new COMTOGTCOAPPLIEDDISCOUNT();
                    comtogtcoapplieddiscountLine.setId(Long.parseLong(request.itemList().get(i).discountList().get(j).id()));
                    comtogtcoapplieddiscountLine.setType(Integer.parseInt(request.itemList().get(i).discountList().get(j).type()));
                    comtogtcoapplieddiscountLine.setValue(new BigDecimal(request.itemList().get(i).discountList().get(j).value()));
                    comtogtcoapplieddiscountList.getAppliedDiscount().add(comtogtcoapplieddiscountLine);
                }
                comtogtcoline.setAppliedDiscountList(comtogtcoapplieddiscountList);
            }
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
}
