package ru.sportmasterlab.Generate_order.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.sportmasterlab.Generate_order.model.OrderDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OrderMapper implements RowMapper<OrderDto> {
    @Override
    public OrderDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new OrderDto(
                rs.getLong("order_code"),
                rs.getString("order_num"),
                rs.getString("csm_status"),
                rs.getString("mars_status"),
                rs.getString("compro_status"),
                rs.getString("kisrm_status"),
                rs.getString("order_spec")
        );
    }
}