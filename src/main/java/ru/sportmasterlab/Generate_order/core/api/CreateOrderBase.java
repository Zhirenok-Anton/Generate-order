package ru.sportmasterlab.Generate_order.core.api;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;
import ru.sm.qaa.soap.gen.ComProCsm.ComCsmApiPortType;
import ru.sm.qaa.soap.gen.ComProLite.ComLiteApiPortType;
import ru.sm.qaa.soap.gen.ComProOGate.ComProOGateApiPortType;
import ru.sm.qaa.soap.gen.ComProPGate.ComPgateApiPortType;
import ru.sm.qaa.soap.gen.MarsGate.MarsGateApiEndpointService;
import ru.sm.qaa.soap.gen.MarsGate.MarsGateApiPortType;
import ru.sportmasterlab.Generate_order.core.directory.Directory;
import ru.sportmasterlab.Generate_order.model.order.created.ItemList;
import ru.sportmasterlab.Generate_order.model.order.created.OrderRequest;

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

    protected static Long getPaymentTypeId(OrderRequest request){
        Long paymentTypeId= null;
        for (int i = 0; i< Directory.paymentsDirectory.size(); i++) {
            if(request.money().paymentType().equals(Directory.paymentsDirectory.get(i).code())){
                paymentTypeId = Long.valueOf(Directory.paymentsDirectory.get(i).idPaymentType());
            }
        }
        return paymentTypeId;
    }

    protected static String getCurrencyCode(OrderRequest request){
        String currencyCode = null;
        for (int i = 0;i<Directory.currencyDirectory.size();i++) {
            if(request.money().currencyCode().equals(Directory.currencyDirectory.get(i).currencyType())){
                currencyCode = String.valueOf(Long.valueOf(Directory.currencyDirectory.get(i).currencyCode()));
            }
        }
        return currencyCode;
    }

    protected static BigDecimal getSumToPayWare(OrderRequest request){
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

    protected static BigDecimal getDiscountTotal(OrderRequest request){
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

    protected static BigDecimal getDiscountTotalWare(ItemList item){
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

    protected static Long getCreditProductId(OrderRequest request){
        Long creditProductId = null;
        for (int i = 0; i< Directory.paymentsDirectory.size(); i++) {
            if(request.money().paymentType().equals(Directory.paymentsDirectory.get(i).code()) && Directory.paymentsDirectory.get(i).idCreditProduct()!=null){
                creditProductId = Long.parseLong(Directory.paymentsDirectory.get(i).idCreditProduct());
            }
        }
        return creditProductId;
    }

    protected static BigDecimal getBonusTotal(){
        return BigDecimal.valueOf(0);
    }

}
