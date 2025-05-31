package ru.sportmasterlab.Generate_order.core.integration;

import ru.sm.qaa.soap.gen.ComProCsm.ComCsmApiPortType;
import ru.sm.qaa.soap.gen.ComProCsm.GetLogisticRequest;
import ru.sm.qaa.soap.gen.ComProCsm.LILogisticDocList;
import ru.sm.qaa.soap.gen.ComProCsm.LILogisticInfo;
import ru.sm.qaa.soap.gen.ComProLite.ComLiteApiPortType;
import ru.sm.qaa.soap.gen.ComProOGate.ComProOGateApiPortType;
import ru.sm.qaa.soap.gen.ComProPGate.ComPgateApiPortType;
import ru.sm.qaa.soap.gen.MarsGate.MarsGateApiEndpointService;
import ru.sm.qaa.soap.gen.MarsGate.MarsGateApiPortType;

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
    static Long idWare = 152043060299L;
    static BigDecimal discountValue = new BigDecimal("100.00");
    static XMLGregorianCalendar TodayDay;
    static Integer sizeWare = 0;
    static Long IdGateEntryPoint = 10130299L;

    //для логирования xml запросов по созданию заказов в компро
    static {
        com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump = true;
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
}
