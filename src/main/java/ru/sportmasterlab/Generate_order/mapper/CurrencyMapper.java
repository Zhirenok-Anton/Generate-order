package ru.sportmasterlab.Generate_order.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.sportmasterlab.Generate_order.model.dataBase.CurrencyDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class CurrencyMapper implements RowMapper<ArrayList<CurrencyDto>> {

    @Override
    public ArrayList<CurrencyDto> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ArrayList<CurrencyDto> listCurrency = new ArrayList<>();
        do{
            listCurrency.add(new CurrencyDto(
                    rs.getLong("id"),
                    rs.getInt("currency_code"),
                    rs.getString("currency_type")));
        }while (rs.next());
        return listCurrency;
    }
}