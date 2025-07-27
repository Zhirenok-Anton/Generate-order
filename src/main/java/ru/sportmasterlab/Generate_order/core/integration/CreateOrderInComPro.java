package ru.sportmasterlab.Generate_order.core.integration;

import ru.sm.qaa.soap.gen.ComProCsm.GetLogisticRequest;
import ru.sm.qaa.soap.gen.ComProCsm.LILogisticDocList;
import ru.sm.qaa.soap.gen.ComProCsm.LILogisticInfo;
import ru.sm.qaa.soap.gen.ComProLite.FindClientOrderRequest;
import ru.sm.qaa.soap.gen.ComProLite.FindClientOrderResponse;
import ru.sm.qaa.soap.gen.ComProOGate.*;
import ru.sm.qaa.soap.gen.MarsGate.SubmitByLinesResponse;
import ru.sportmasterlab.Generate_order.core.directory.Directory;
import ru.sportmasterlab.Generate_order.model.order.created.ItemList;
import ru.sportmasterlab.Generate_order.model.order.created.OrderRequest;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CreateOrderInComPro extends CreateOrderBase {

    public static final String EXECUTE_COM_PRO_RESERVE_CONSIGNEMNT = "CALL COM.COM_UI_ORDER_API.RESERVE_CONSIGNEMNT(?, 1)";
    public static final String EXECUTE_COM_PRO_CONFIRM_CONSIGNMENT = "CALL COM.COM_UI_ORDER_API.CONFIRM_CONSIGNMENT(?)";

    public static CreateOrderResponse createOrderWareInComPro(OrderRequest request, SubmitByLinesResponse submitResponse) {
        setOrderNum(submitResponse.getCalculations().getCalcSubmit().getFirst().getOrderNum());
        CreateOrderRequest createOrderRequest =
                createComproOrderRequest(request,submitResponse);
        return comProOGateApiPortType.createOrder(createOrderRequest);
    }

    private static CreateOrderRequest createComproOrderRequest(OrderRequest request, SubmitByLinesResponse submitResponse) {
        setTodayDate();

        //TODO: ОБЯЗАТЕЛЬНО УБРАТЬ КОСТЫЛЬ
        sumToPayWare = getSumToPayWare(request);

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
        comtogtcomoney.setBonusTotal(getBonusTotal(request));
        comtogtcomoney.setPayType(1);//хардкод
        comtogtcomoney.setPaymentTypeId(getPaymentTypeId(request));

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

    public static LILogisticInfo getLogistic(Long orderCode) {
        GetLogisticRequest getLogisticRequest = new GetLogisticRequest();
        getLogisticRequest.setOrderCode(BigDecimal.valueOf(orderCode));
        return comCsmApiPortType.getLogistic(getLogisticRequest).getLogisticInfo();
    }

    public static FindClientOrderResponse getComproOrder(OrderRequest request, CreateOrderResponse createOrderResponse){

        FindClientOrderRequest findClientOrderRequest = new FindClientOrderRequest();
        findClientOrderRequest.setOrderCode(BigInteger.valueOf(createOrderResponse.getOrderCode()));
        findClientOrderRequest.setShopNum(new BigInteger(request.shopNum()));
        return comLiteApiPortType.findClientOrder(findClientOrderRequest);
    }

    public static void setStatusReserve(Long orderCode, BigDecimal consignmentCode) {
        OracleDBCompro jdbi = new OracleDBCompro();
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

    private static Long getPaymentTypeId(OrderRequest request){
        Long paymentTypeId= null;
        for (int i = 0;i<Directory.paymentsDirectory.size();i++) {
            if(request.money().paymentType().equals(Directory.paymentsDirectory.get(i).code())){
                paymentTypeId = Long.valueOf(Directory.paymentsDirectory.get(i).idPaymentType());
            }
        }
        return paymentTypeId;
    }

    private static String getCurrencyCode(OrderRequest request){
        String currencyCode = null;
        for (int i = 0;i<Directory.currencyDirectory.size();i++) {
            if(request.money().currencyCode().equals(Directory.currencyDirectory.get(i).currencyType())){
                currencyCode = String.valueOf(Long.valueOf(Directory.currencyDirectory.get(i).currencyCode()));
            }
        }
        return currencyCode;
    }

    private static BigDecimal getSumToPayWare(OrderRequest request){
        BigDecimal sumToPayWare = BigDecimal.valueOf(0);
        for (int i = 0; i<request.itemList().size(); i++){
            BigDecimal sumDiscountWare = BigDecimal.valueOf(0);
            if (request.itemList().get(i).discountList()!= null) {
                for (int j = 0; j < request.itemList().get(i).discountList().size(); j++) {
                    if (request.itemList().get(i).discountList().get(j).type().equals("1")) {
                        sumDiscountWare = sumDiscountWare.add(new BigDecimal(request.itemList().get(i).discountList().get(j).value()));
                    }
                }
            }
            //sum = ( price - discount ) * qtyOrdered
            sumToPayWare = sumToPayWare.add(
                    (new BigDecimal(request.itemList().get(i).price())
                            .multiply(new BigDecimal(request.itemList().get(i).qtyOrdered()))
                            .subtract(sumDiscountWare)));
        }
        return sumToPayWare;
    }

    private static BigDecimal getDiscountTotal(OrderRequest request){
        BigDecimal sumDiscount = BigDecimal.valueOf(0);
        for (int i = 0; i<request.itemList().size(); i++){
            if (request.itemList().get(i).discountList()!= null){
                for (int j = 0; j < request.itemList().get(i).discountList().size(); j++) {
                    if(request.itemList().get(i).discountList().get(j).type().equals("1")){
                        sumDiscount = sumDiscount.add(new BigDecimal(request.itemList().get(i).discountList().get(j).value()));
                    }
                }
            }
        }
        return sumDiscount;
    }

    private static BigDecimal getDiscountTotalWare(ItemList item){
        BigDecimal sumDiscountWare = BigDecimal.valueOf(0);
        if (item.discountList()!= null) {
            for (int i = 0; i < item.discountList().size(); i++) {
                if (item.discountList().get(i).type().equals("1")) {
                    sumDiscountWare = sumDiscountWare.add(new BigDecimal(item.discountList().get(i).value()));
                }
            }
        }
        return sumDiscountWare;
    }

    private static BigDecimal getBonusTotal(OrderRequest request){
        return BigDecimal.valueOf(0);
    }
}
