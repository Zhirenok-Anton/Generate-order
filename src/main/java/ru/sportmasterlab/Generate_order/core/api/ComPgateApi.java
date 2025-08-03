package ru.sportmasterlab.Generate_order.core.api;

import ru.sm.qaa.soap.gen.ComProOGate.CreateOrderResponse;
import ru.sm.qaa.soap.gen.ComProPGate.*;
import ru.sportmasterlab.Generate_order.model.order.created.OrderRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

public class ComPgateApi extends CreateOrderBase {

    public static CreatePaymentResponse getCreatePaymentResponse(OrderRequest request, CreateOrderResponse createOrderResponse) {
        CreatePaymentRequest createPaymentRequest = null;

        if(!request.money().paymentType().equals("IN_SHOP")){
            createPaymentRequest = сreatePaymentRequest(request, createOrderResponse);
        }else {
            return null;
        }
        return comPgateApiPortType.createPayment(createPaymentRequest);
    }

     private static CreatePaymentRequest сreatePaymentRequest(OrderRequest request, CreateOrderResponse createOrderResponse) {
        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest();
        createPaymentRequest.setEntryPoint(BigDecimal.valueOf(10140299));
        createPaymentRequest.setSessionId("23545-23434-" + getRandomNumber(7));
        createPaymentRequest.setShopNum(BigDecimal.valueOf(Long.parseLong(request.shopNum())));
        createPaymentRequest.setBasketNum("ОП-" + orderNum);
        createPaymentRequest.setFundDate(TodayDay);
        createPaymentRequest.setPaymentState(BigDecimal.valueOf(1));

        CPBind cpBind = new CPBind();
        cpBind.setOrderCode(BigDecimal.valueOf(createOrderResponse.getOrderCode()));
        cpBind.setKisAvansNum(getRandomNumber(29));
        cpBind.setBindSum(getSumToPayWare(request));

        createPaymentRequest.setBind(cpBind);
        createPaymentRequest.setPayment(getCPPayment(request));

        return createPaymentRequest;
    }

    private static CPPayment getCPPayment (OrderRequest request){
        CPPayment cpPayment = null;
        if(request.money().paymentType().equals("BANK_CARD")){
            cpPayment = setBankCardPayment(request);
        }
        if(request.money().paymentType().equals("IS_CREDIT_TINCOFF") || request.money().paymentType().equals("SPLIT")){
            cpPayment = setCreditPayment(request);
        }
        if (request.money().paymentType().equals("PC")){
            cpPayment = setCpGiftCardPayment(request);
        }
        return cpPayment;
    }

    //Создание платежа ЭПК
    private static CPPayment setCpGiftCardPayment(OrderRequest request) {
        CPPayment cpPayment = setCPPayment(request);
        CPGiftCard cpGiftCard = new CPGiftCard();
        cpGiftCard.setCardNum(new BigDecimal(getEPCNumber()));
        cpGiftCard.setCardNominal(BigDecimal.valueOf(1000));
        cpPayment.setGiftCard(cpGiftCard);
        return cpPayment;
    }

    //создание данных для платежа банковской картой
    private static CPPayment setBankCardPayment(OrderRequest request) {
        CPPayment cpPayment = setCPPayment(request);
        cpPayment.setBankCard(setCPBankCard());
        return cpPayment;
    }

   // Создание платежа кредитной картой
    private static CPPayment setCreditPayment(OrderRequest request) {
        CPPayment cpPayment = setCPPayment(request);
        cpPayment.setBankCard(setCPBankCard());
        cpPayment.setCreditProductId(BigDecimal.valueOf(getCreditProductId(request)));
        return cpPayment;
    }

    private static CPBankCard setCPBankCard (){
        CPBankCard cpBankCard = new CPBankCard();
        cpBankCard.setCardKind(BigDecimal.valueOf(14840299));
        cpBankCard.setCardNumMask("547927XXXXXX6583");
        cpBankCard.setProcessingCode(BigDecimal.valueOf(10400299));
        cpBankCard.setIdProcessTrans("934391039154");
        return cpBankCard;
    }

    private static CPPayment setCPPayment (OrderRequest request){
        CPPayment cpPayment = new CPPayment();
        cpPayment.setPaymentNum(paymentGateIdFrom("29E08ABG3269401JD", 15));
        cpPayment.setPaymentGateId(paymentGateIdFrom("29E08ABG3269401JD", 15));
        cpPayment.setPaymentDate(TodayDay);
        cpPayment.setPaymentSum(getSumToPayWare(request));
        return cpPayment;
    }

    private static String paymentGateIdFrom(String baseId, int length) {
        return baseId + getRandomNumber(length);
    }

    private static String getRandomNumber(int n) {
        String AlphaNumber = "0123456789";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index
                    = (int) (AlphaNumber.length()
                    * Math.random());
            sb.append(AlphaNumber
                    .charAt(index));
        }
        return sb.toString();
    }

    //TODO: создать справочник
    private static String getEPCNumber() {
        ArrayList<String> epcList = new ArrayList<>();
        epcList.add("123123");
        epcList.add("321312");
        int randomIndex = new Random().nextInt(epcList.size());
        return epcList.get(randomIndex);
    }
}