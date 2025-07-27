package ru.sportmasterlab.Generate_order.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.sportmasterlab.Generate_order.mapper.CurrencyMapper;
import ru.sportmasterlab.Generate_order.mapper.PaymentsMapper;
import ru.sportmasterlab.Generate_order.model.dataBase.CurrencyDto;
import ru.sportmasterlab.Generate_order.model.dataBase.PaymentsDto;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public class AdminRepositoryImpl implements AdminRepository{

    private static final String SQL_GET_PAYMENTS =
            "SELECT id, \"name\", code, id_payment_type, id_credit_product, is_actual \n" +
            "FROM public.payments";

    private static final String SQL_GET_CURRENCY =
            "SELECT id, currency_code, currency_type \n" +
            "FROM public.currency";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PaymentsMapper paymentsMapper;
    private final CurrencyMapper currencyMapper;

    public AdminRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate, PaymentsMapper paymentsMapper, CurrencyMapper currencyMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.paymentsMapper = paymentsMapper;
        this.currencyMapper = currencyMapper;
    }

    @Override
    public Optional<ArrayList<PaymentsDto>> updatePayments() {
        var params = new MapSqlParameterSource();
        return jdbcTemplate.query(
                        SQL_GET_PAYMENTS,
                        params,
                        paymentsMapper
                ).stream()
                .findFirst();
    }

    @Override
    public Optional<ArrayList<CurrencyDto>> updateCurrency() {
        var params = new MapSqlParameterSource();
        return jdbcTemplate.query(
                        SQL_GET_CURRENCY,
                        params,
                        currencyMapper
                ).stream()
                .findFirst();
    }
}