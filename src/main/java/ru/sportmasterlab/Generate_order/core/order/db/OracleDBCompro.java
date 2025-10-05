package ru.sportmasterlab.Generate_order.core.order.db;

import org.jdbi.v3.core.Jdbi;

import java.math.BigDecimal;

public class OracleDBCompro {
    private final static Jdbi jdbiComPro = Jdbi.create("jdbc:oracle:thin:com_exe/com_exe@//ex8td-scan.gksm.local:1521/sks_etalon_compro_ru");

    //Вызвать процедуру c условиями
    public void oneExecute(String query, BigDecimal consignmentCode) {
        jdbiComPro.useTransaction(handle ->
                handle.execute(query, consignmentCode));
    }
}