package ru.sportmasterlab.Generate_order.core.order.integration;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;
import ru.sm.qaa.soap.gen.ComProCsm.ComCsmApiPortType;
import ru.sm.qaa.soap.gen.ComProLite.ComLiteApiPortType;
import ru.sm.qaa.soap.gen.ComProOGate.ComProOGateApiPortType;
import ru.sm.qaa.soap.gen.ComProPGate.ComPgateApiPortType;
import ru.sm.qaa.soap.gen.MarsGate.MarsGateApiEndpointService;
import ru.sm.qaa.soap.gen.MarsGate.MarsGateApiPortType;
import ru.sportmasterlab.Generate_order.core.admin.directory.Directory;
import ru.sportmasterlab.Generate_order.model.admin.CurrencyDto;
import ru.sportmasterlab.Generate_order.model.admin.PaymentsDto;
import ru.sportmasterlab.Generate_order.model.order.created.DiscountList;
import ru.sportmasterlab.Generate_order.model.order.created.ItemList;
import ru.sportmasterlab.Generate_order.model.order.created.OrderRequestDto;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateOrderBase {
    protected static final ComLiteApiPortType comLiteApiPortType =
            new ru.sm.qaa.soap.gen.ComProLite.ServiceApiEndpointService().getServiceApiEndpointPort();
    protected static final MarsGateApiPortType marsGateApiPortType =
            new MarsGateApiEndpointService().getMarsGateApiEndpointPort();
    protected static final ComProOGateApiPortType comProOGateApiPortType =
            new ru.sm.qaa.soap.gen.ComProOGate.ServiceApiEndpointService().getServiceApiEndpointPort();
    protected static final ComPgateApiPortType comPgateApiPortType =
            new ru.sm.qaa.soap.gen.ComProPGate.ServiceApiEndpointService().getServiceApiEndpointPort();
    protected static final ComCsmApiPortType comCsmApiPortType =
            new ru.sm.qaa.soap.gen.ComProCsm.ServiceApiEndpointService().getServiceApiEndpointPort();

    static String orderNum;
    static XMLGregorianCalendar TodayDay;
    static Long IdGateEntryPoint = 10130299L;

    //для логирования xml запросов по созданию заказов в компро
    static {
        HttpTransportPipe.dump = true;
    }

    protected static void setOrderNum(String orderNum){
        CreateOrderBase.orderNum = orderNum;
    }

    protected static void setTodayDate() {
        String FORMATER = "yyyy-MM-dd'T'HH:mm:ss" + "+03:00";
        DateFormat format = new SimpleDateFormat(FORMATER);
        try {
            TodayDay = DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(new Date()));
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    protected static Long getPaymentTypeId(OrderRequestDto request){
        Long paymentTypeId= null;
        for (PaymentsDto payment : Directory.paymentsDirectory){
            if(request.money().paymentType().equals(payment.code())){
                paymentTypeId = Long.valueOf(payment.idPaymentType());
            }
        }
        return paymentTypeId;
    }

    protected static String getCurrencyCode(OrderRequestDto request){
        String currencyCode = null;
        for (CurrencyDto currency : Directory.currencyDirectory){
            if(request.money().currencyCode().equals(currency.currencyType())){
                currencyCode = String.valueOf(Long.valueOf(currency.currencyCode()));
            }
        }
        return currencyCode;
    }

    protected static BigDecimal getSumToPayWare(OrderRequestDto request){
        BigDecimal sumToPayWare = BigDecimal.valueOf(0);
        for(ItemList item: request.itemList()){
            BigDecimal sumDiscountWare = BigDecimal.valueOf(0);
            if (item.discountList()!= null) {
                for(DiscountList discount: item.discountList()){
                    if (discount.type().equals("1")) {
                        sumDiscountWare = sumDiscountWare.add(new BigDecimal(discount.value()));
                    }
                }
            }
            //sum = ( price - discount ) * qtyOrdered
            sumToPayWare = sumToPayWare.add(new BigDecimal(item.price()).subtract(sumDiscountWare).multiply(new BigDecimal(item.qtyOrdered())));
        }
        return sumToPayWare;
    }

    protected static BigDecimal getDiscountTotal(OrderRequestDto request){
        BigDecimal sumDiscount = BigDecimal.valueOf(0);
        for(ItemList item: request.itemList()){
            if (item.discountList()!= null){
                for(DiscountList discount: item.discountList()){
                    if(discount.type().equals("1")){
                        sumDiscount = sumDiscount.add(new BigDecimal(discount.value()));
                    }
                }
            }
        }
        return sumDiscount;
    }

    protected static BigDecimal getDiscountTotalWare(ItemList item){
        BigDecimal sumDiscountWare = BigDecimal.valueOf(0);
        if (item.discountList()!= null) {
            for(DiscountList discount: item.discountList()){
                if (discount.type().equals("1")) {
                    sumDiscountWare = sumDiscountWare.add(new BigDecimal(discount.value()));
                }
            }
        }
        return sumDiscountWare;
    }

    protected static Long getCreditProductId(OrderRequestDto request){
        Long creditProductId = null;
        for (PaymentsDto payment : Directory.paymentsDirectory){
            if(request.money().paymentType().equals(payment.code()) && payment.idCreditProduct()!=null){
                creditProductId = Long.parseLong(payment.idCreditProduct());
            }
        }
        return creditProductId;
    }

    protected static BigDecimal getBonusTotal(){
        return BigDecimal.valueOf(0);
    }

}
