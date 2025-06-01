package ru.sportmasterlab.Generate_order.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.sportmasterlab.Generate_order.mapper.OrderMapper;
import ru.sportmasterlab.Generate_order.model.OrderDto;

import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private static final String SQL_GET_ORDER_BY_CODE =
            "SELECT order_code, csm_status, mars_status, compro_status, kisrm_status, order_spec \n" +
            "FROM public.order_status \n" +
            "WHERE order_code= :order_code";

    private static final String SQL_INSERT_ORDER = "" +
            "INSERT INTO public.order_status\n" +
            "(order_code, csm_status, mars_status, compro_status, kisrm_status, order_spec)\n" +
            "VALUES(:orderCode, :csmStatus, :marsStatus, :comproStatus, :kisrmStatus, :order);";

    private static final String SQL_UPDATE_ORDER = "" +
            "UPDATE public.order_status \n" +
            "SET csm_status = :csmStatus, mars_status = :marsStatus, compro_status = :comproStatus, kisrm_status = :kisrmStatus, order_spec = :order \n" +
            "WHERE order_code= :orderCode;";

    private final OrderMapper orderMapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderRepositoryImpl(OrderMapper orderMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        this.orderMapper = orderMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<OrderDto> getOrderByCode(Long orderCode) {
        var params = new MapSqlParameterSource();

       params.addValue("order_code", String.valueOf(orderCode));

        return jdbcTemplate.query(
                        SQL_GET_ORDER_BY_CODE,
                        params,
                        orderMapper
                ).stream()
                .findFirst();
    }

    @Override
    public void insertOrder(Long orderCode, String csmStatus, String marsStatus, String comproStatus, String kisrmStatus, String order) {
        var params = new MapSqlParameterSource();
        params.addValue("orderCode",orderCode);
        params.addValue("csmStatus",csmStatus);
        params.addValue("marsStatus", marsStatus);
        params.addValue("comproStatus",comproStatus);
        params.addValue("kisrmStatus", kisrmStatus);
        params.addValue("order",order);
        jdbcTemplate.update(SQL_INSERT_ORDER,params);
    }

    @Override
    public void updateOrder(Long orderCode, String csmStatus, String marsStatus, String comproStatus, String kisrmStatus, String order) {
        var params = new MapSqlParameterSource();
        params.addValue("orderCode",orderCode);
        params.addValue("csmStatus",csmStatus);
        params.addValue("marsStatus", marsStatus);
        params.addValue("comproStatus",comproStatus);
        params.addValue("kisrmStatus", kisrmStatus);
        params.addValue("order",order);
        jdbcTemplate.update(SQL_UPDATE_ORDER,params);
    }

    @Override
    public void deleteProfileById(String id) {
        //нет необходимости
    }
}
