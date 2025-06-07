package ru.sportmasterlab.Generate_order.core.integration;

import org.jdbi.v3.core.Jdbi;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
public class OracleDBService {
    private final static Jdbi jdbi = Jdbi.create("jdbc:oracle:thin:SHOP6945_MS_TEST/\"ms@test\"@//oralistener-test.gksm.local:1521/salesapi_test");
    private final static Jdbi jdbiIntraday = Jdbi.create("");
    private final static Jdbi jdbiComPro = Jdbi.create("jdbc:oracle:thin:com_exe/com_exe@//ex8td-scan.gksm.local:1521/sks_etalon_compro_ru");

   // @Step("Выполняем запрос в БД для получения количества записей")
    public int getCount(final String query) {

        try {
            return jdbi.withHandle(handle ->
                    handle.select(query)
                            .mapTo(Integer.class).first());
        } catch (IllegalStateException illegalStateException) {
            return 0;
        }
    }

    //@Step("Выполняем запрос в БД для получения количества записей по условию")
    public int getCount(final String query, Object paramValue) {
        try {
            return jdbi.withHandle(handle ->
                    handle.select(query, paramValue).
                            mapTo(Integer.class).first());
        } catch (IllegalStateException illegalStateException) {
            return 0;
        }
    }

    //@Step("Выполняем запрос в БД для получения списка значений")
    public List<Map<String, Object>> getValues(final String query) {
        return jdbi.withHandle(handle ->
                handle.select(query)
                        .mapToMap()
                        .list());
    }

    //@Step("Выполняем запрос в БД для получения списка значений по заданным параметрам")
    public List<Map<String, Object>> getValues(final String query, Object paramValue) {
        return jdbi.withHandle(handle ->
                handle.select(query, paramValue)
                        .mapToMap()
                        .list());
    }

    //@Step("Выполняем запрос в БД для получения списка значений по заданным параметрам")
    public List<String> getListOfValues(final String query, Object paramValue) {
        return jdbi.withHandle(handle ->
                handle.select(query, paramValue)
                        .mapTo(String.class)
                        .list());
    }

    //@Step("Выполняем запрос в БД для получения определенного значения")
    public String getOneValue(final String query, Object paramValue) {
        return jdbi.withHandle(handle ->
                handle.select(query, paramValue)
                        .mapTo(String.class).first());
    }

    //@Step("Выполняем запрос в БД для получения определенного значения")
    public String getOneValue(final String query, Object... paramValues) {
        return jdbi.withHandle(handle ->
                handle.select(query, paramValues)
                        .mapTo(String.class).first());
    }

    //@Step("Выполнить запрос")
    public void execute(String query) {
        jdbi.withHandle(handle ->
                handle.execute(query));
    }

    //@Step("Выполнить запрос c условиями")
    public void execute(String query, Object... paramValues) {
        jdbi.withHandle(handle ->
                handle.execute(query, paramValues));
    }

    //@Step("Вызвать процедуры c условиями")
    public void multiExecute(List<String> query, BigDecimal consignmentCode) {
        jdbiComPro.useTransaction(handle -> {
            for (String s : query) handle.execute(s, consignmentCode);
        });
    }

    //@Step("Вызвать процедуру c условиями")
    public void oneExecute(String query, BigDecimal consignmentCode) {
        jdbiComPro.useTransaction(handle ->
                handle.execute(query, consignmentCode));
    }

    //@Step("Выполняем запрос в БД магазина 6079 для получения определенного значения по сервисному заказу")
    public String getOneValueFromServiceOrder(final String query, Object paramValue) {
        return jdbiIntraday.withHandle(handle ->
                handle.select(query, paramValue)
                        .mapTo(String.class).first());
    }

    //@Step("Выполняем запрос в БД магазина 6079 для получения списка значений по сервисному заказу")
    public List<Map<String, Object>> getValuesFromServiceOrder(final String query, Object paramValue) {
        return jdbiIntraday.withHandle(handle ->
                handle.select(query, paramValue)
                        .mapToMap()
                        .list());
    }
}