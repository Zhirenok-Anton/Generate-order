package ru.sportmasterlab.Generate_order.core.integration;

import ru.sm.qaa.soap.gen.ComProOGate.CreateOrderResponse;
import ru.sm.qaa.soap.gen.ComProPGate.*;
import ru.sm.qaa.soap.gen.MarsGate.SubmitByLinesResponse;
import ru.sm.qaa.soap.gen.MarsGate.TCalcSubmit;


import java.math.BigDecimal;

public class CreatePayment extends CreateOrderBase {
    //@Step("Создание платежа")
   /* public static CreatePaymentRequest createPaymentRequest(
            SubmitByLinesResponse submitResponse, CreateOrderResponse createOrderResponse,
            CPPayment cpPayment, int quantity) {
        TCalcSubmit tCalcSubmit = submitResponse.getCalculations().getCalcSubmit().getFirst();

        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest();
        createPaymentRequest.setEntryPoint(BigDecimal.valueOf(10140299));
        createPaymentRequest.setSessionId("23545-23434-" + getRandomNumber(7));
        createPaymentRequest.setShopNum(BigDecimal.valueOf(Long.parseLong(cfg.ShopID())));
        createPaymentRequest.setBasketNum("ОП-" + tCalcSubmit.getOrderNum());
        createPaymentRequest.setFundDate(TodayDay);
        createPaymentRequest.setPaymentState(BigDecimal.valueOf(1));

        BigDecimal sumToPayWare = new BigDecimal("0.0").add(
                priceWare.multiply(BigDecimal.valueOf(quantity)));
        CPBind cpBind = new CPBind();
        cpBind.setOrderCode(BigDecimal.valueOf(createOrderResponse.getOrderCode()));
        cpBind.setKisAvansNum(getRandomNumber(29));
        cpBind.setBindSum(sumToPayWare);

        createPaymentRequest.setBind(cpBind);
        createPaymentRequest.setPayment(cpPayment);

        return createPaymentRequest;
    }*/

    //@Step("Создание платежа")
    public static CreatePaymentRequest createPaymentRequest(String shopNum,
            SubmitByLinesResponse submitResponse, CreateOrderResponse createOrderResponse,
            CPPayment cpPayment, int quantity, Double price) {
        TCalcSubmit tCalcSubmit = submitResponse.getCalculations().getCalcSubmit().getFirst();

        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest();
        createPaymentRequest.setEntryPoint(BigDecimal.valueOf(10140299));
        createPaymentRequest.setSessionId("23545-23434-" + getRandomNumber(7));
        createPaymentRequest.setShopNum(BigDecimal.valueOf(Long.parseLong(shopNum)));
        createPaymentRequest.setBasketNum("ОП-" + tCalcSubmit.getOrderNum());
        createPaymentRequest.setFundDate(TodayDay);
        createPaymentRequest.setPaymentState(BigDecimal.valueOf(1));

        BigDecimal sumToPayWare = new BigDecimal("0.0").add(BigDecimal.valueOf(
                price).multiply(BigDecimal.valueOf(quantity)));

        CPBind cpBind = new CPBind();
        cpBind.setOrderCode(BigDecimal.valueOf(createOrderResponse.getOrderCode()));
        cpBind.setKisAvansNum(getRandomNumber(29));
        cpBind.setBindSum(sumToPayWare);

        createPaymentRequest.setBind(cpBind);
        createPaymentRequest.setPayment(cpPayment);

        return createPaymentRequest;
    }

    /*@Step("Создание платежа ЭПК")
    public static CPPayment setCpGiftCardPayment(int quantity) {
        CPPayment cpPayment = new CPPayment();
        cpPayment.setPaymentNum(paymentGateIdFrom("29E08ABG3269401JD", 15));
        cpPayment.setPaymentGateId(paymentGateIdFrom("29E08ABG3269401JD", 15));
        cpPayment.setPaymentDate(TodayDay);
        BigDecimal sumToPayWare = new BigDecimal("0.0").add(
                priceWare.multiply(BigDecimal.valueOf(quantity)));
        cpPayment.setPaymentSum(sumToPayWare);
        CPGiftCard cpGiftCard = new CPGiftCard();
        cpGiftCard.setCardNum(new BigDecimal(getEPCNumber()));
        cpGiftCard.setCardNominal(BigDecimal.valueOf(1000));
        cpPayment.setGiftCard(cpGiftCard);
        return cpPayment;
    }*/

    /*@Step("Создание платежа БК")
    public static CPPayment setBankCardPayment(int quantity) {
        CPPayment cpPayment = new CPPayment();
        cpPayment.setPaymentNum(paymentGateIdFrom("29E08ABG3269401JD", 15));
        cpPayment.setPaymentGateId(paymentGateIdFrom("29E08ABG3269401JD", 15));
        cpPayment.setPaymentDate(TodayDay);
        BigDecimal sumToPayWare = new BigDecimal("0.0").add(
                priceWare.multiply(BigDecimal.valueOf(quantity)));
        cpPayment.setPaymentSum(sumToPayWare);
        CPBankCard cpBankCard = new CPBankCard();
        cpBankCard.setCardKind(BigDecimal.valueOf(14840299));
        cpBankCard.setCardNumMask("547927XXXXXX6583");
        cpBankCard.setProcessingCode(BigDecimal.valueOf(10400299));
        cpBankCard.setIdProcessTrans("934391039154");
        cpPayment.setBankCard(cpBankCard);
        return cpPayment;
    }*/

    //@Step("Создание платежа БК")!!!!!!!!!!!
    public static CPPayment setBankCardPayment(int quantity, Double price) {
        CPPayment cpPayment = new CPPayment();
        cpPayment.setPaymentNum(paymentGateIdFrom("29E08ABG3269401JD", 15));
        cpPayment.setPaymentGateId(paymentGateIdFrom("29E08ABG3269401JD", 15));
        cpPayment.setPaymentDate(TodayDay);
        BigDecimal sumToPayWare = new BigDecimal("0.0").add(
                BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(quantity)));
        cpPayment.setPaymentSum(sumToPayWare);
        CPBankCard cpBankCard = new CPBankCard();
        cpBankCard.setCardKind(BigDecimal.valueOf(14840299));
        cpBankCard.setCardNumMask("547927XXXXXX6583");
        cpBankCard.setProcessingCode(BigDecimal.valueOf(10400299));
        cpBankCard.setIdProcessTrans("934391039154");
        cpPayment.setBankCard(cpBankCard);
        return cpPayment;
    }

   /* @Step("Создание платежа кредитной картой")
    public static CPPayment setCreditPayment(int quantity) {
        CPPayment cpPayment = new CPPayment();
        cpPayment.setPaymentNum(paymentGateIdFrom("29E08ABG3269401JD", 15));
        cpPayment.setPaymentGateId(paymentGateIdFrom("29E08ABG3269401JD", 15));
        cpPayment.setPaymentDate(TodayDay);
        BigDecimal sumToPayWare = new BigDecimal("0.0").add(
                priceWare.multiply(BigDecimal.valueOf(quantity)));
        cpPayment.setPaymentSum(sumToPayWare);
        CPBankCard cpBankCard = new CPBankCard();
        cpBankCard.setCardKind(BigDecimal.valueOf(14840299));
        cpBankCard.setCardNumMask("547927XXXXXX6583");
        cpBankCard.setProcessingCode(BigDecimal.valueOf(10400299));
        cpBankCard.setIdProcessTrans("934391039154");
        cpPayment.setBankCard(cpBankCard);
        cpPayment.setCreditProductId(BigDecimal.valueOf(11610299));
        return cpPayment;
    }*/

    /*@Step("Оплата заказа БК/Кредитом/ЕПК")
    public static CreatePaymentResponse createPaymentResponseByType(
            SubmitByLinesResponse submitResponse, CreateOrderResponse createOrderResponse,
            CPPayment cpPayment, int quantity) {
        CreatePaymentRequest createPaymentRequest = createPaymentRequest(submitResponse, createOrderResponse,
                cpPayment, quantity,);
        return comPgateApiPortType.createPayment(createPaymentRequest);
    }*/

   // @Step("Оплата заказа БК/Кредитом/ЕПК с заданной ценой")
    public static CreatePaymentResponse createPaymentResponseByType(String shopNum,
            SubmitByLinesResponse submitResponse, CreateOrderResponse createOrderResponse,
            CPPayment cpPayment, int quantity, Double price) {
        CreatePaymentRequest createPaymentRequest = createPaymentRequest(shopNum, submitResponse, createOrderResponse,
                cpPayment, quantity, price);
        return comPgateApiPortType.createPayment(createPaymentRequest);
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

    //метод для рандомного выбора одной из ЭПК из списка
    /*private static String getEPCNumber() {
        try {
            File file = new File("src/main/resources/data/testDataEPC.json");
            ObjectMapper objectMapper = new ObjectMapper();
            TestDataEPCDto testDataEPCDto
                    = objectMapper.readValue(file, TestDataEPCDto.class);
            List<String> epcList = testDataEPCDto.getEpc();
            int randomIndex = new Random().nextInt(epcList.size());
            return epcList.get(randomIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
}