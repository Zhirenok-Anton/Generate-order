package ru.sportmasterlab.Generate_order.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.sportmasterlab.Generate_order.model.dataBase.PaymentsDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class PaymentsMapper implements RowMapper<ArrayList<PaymentsDto>> {

    @Override
    public ArrayList<PaymentsDto> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ArrayList<PaymentsDto> listPayments = new ArrayList<>();
        do {
            listPayments.add(new PaymentsDto(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("code"),
                    rs.getString("id_payment_type"),
                    rs.getString("id_credit_product"),
                    rs.getBoolean("is_actual")));
        } while (rs.next());
        return listPayments;
    }
}
