package ru.sportmasterlab.Generate_order.core.api;

import ru.sm.qaa.soap.gen.ComProCsm.GetLogisticRequest;
import ru.sm.qaa.soap.gen.ComProCsm.GetLogisticResponse;
import ru.sm.qaa.soap.gen.ComProCsm.LILogisticDocList;
import ru.sm.qaa.soap.gen.ComProCsm.LILogisticInfo;

import java.math.BigDecimal;

public class ComCsmApi extends CreateOrderBase{

    public static final String EXECUTE_COM_PRO_RESERVE_CONSIGNEMNT = "CALL COM.COM_UI_ORDER_API.RESERVE_CONSIGNEMNT(?, 1)";
    public static final String EXECUTE_COM_PRO_CONFIRM_CONSIGNMENT = "CALL COM.COM_UI_ORDER_API.CONFIRM_CONSIGNMENT(?)";

    public static GetLogisticResponse getGetLogisticResponse(Long orderCode) {
        GetLogisticRequest getLogisticRequest = getLogisticRequest(orderCode);
        return comCsmApiPortType.getLogistic(getLogisticRequest);
    }

    private static GetLogisticRequest getLogisticRequest(Long orderCode){
        GetLogisticRequest getLogisticRequest = new GetLogisticRequest();
        getLogisticRequest.setOrderCode(BigDecimal.valueOf(orderCode));
        return getLogisticRequest;
    }

    //TODO: возможно стоит вынести логику из этого класса
    public static void setStatusReserve(Long orderCode, BigDecimal consignmentCode) {
        OracleDBCompro jdbi = new OracleDBCompro();
        LILogisticInfo liLogisticInfo;
        BigDecimal logisiticDocState;
        int logisiticDocStateInt = 1;

        while (logisiticDocStateInt == 1) {
            jdbi.oneExecute(EXECUTE_COM_PRO_RESERVE_CONSIGNEMNT, consignmentCode);
            liLogisticInfo = getGetLogisticResponse(orderCode).getLogisticInfo();
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
