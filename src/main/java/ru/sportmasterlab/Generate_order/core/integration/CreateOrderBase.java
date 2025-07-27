package ru.sportmasterlab.Generate_order.core.integration;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;
import ru.sm.qaa.soap.gen.ComProCsm.ComCsmApiPortType;
import ru.sm.qaa.soap.gen.ComProLite.ComLiteApiPortType;
import ru.sm.qaa.soap.gen.ComProOGate.ComProOGateApiPortType;
import ru.sm.qaa.soap.gen.ComProPGate.ComPgateApiPortType;
import ru.sm.qaa.soap.gen.MarsGate.MarsGateApiEndpointService;
import ru.sm.qaa.soap.gen.MarsGate.MarsGateApiPortType;
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
    static BigDecimal discountValue = new BigDecimal("0.0");

    static BigDecimal sumToPayWare = new BigDecimal("0.0");
    static String orderNum;
    static XMLGregorianCalendar TodayDay;
    static Long IdGateEntryPoint = 10130299L;

    //для логирования xml запросов по созданию заказов в компро
    static {
        HttpTransportPipe.dump = true;
    }

    protected static void setOrderNum(){
        orderNum =  (int) (Math.random()*1000000)+ "-" + (int) (Math.random()*1000000);
    }
    protected static void setOrderNum(String orderNum){
        CreateOrderBase.orderNum = orderNum;
    }

    //TODO: похоже что уже бесполезный метод
    protected static void setDefaultValue(){
        sumToPayWare = new BigDecimal("0.0");
        discountValue = new BigDecimal("0.0");
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

    protected static void setSumToPayWare(OrderRequest request){
        for (int i = 0; i<request.itemList().size(); i++){
            sumToPayWare = sumToPayWare.add(new BigDecimal(request.itemList().get(i).qtyOrdered()).multiply(new BigDecimal(request.itemList().get(i).price())));
        }
    }
}
