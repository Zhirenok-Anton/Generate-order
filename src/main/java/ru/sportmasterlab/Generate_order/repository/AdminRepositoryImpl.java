package ru.sportmasterlab.Generate_order.repository;

import org.springframework.stereotype.Repository;

import ru.sportmasterlab.Generate_order.model.admin.ResponseCurrencyDto;
import ru.sportmasterlab.Generate_order.model.admin.ResponsePaymentsDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

@Repository
public class AdminRepositoryImpl implements AdminRepository{

    public AdminRepositoryImpl() {
    }

    @Override
    public ResponsePaymentsDto updatePayments() {
        ResponsePaymentsDto responsePaymentsDto;

        try {
            File file = new File("src/main/resources/directory/payments.json");
            responsePaymentsDto = new ObjectMapper().readValue(file, ResponsePaymentsDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responsePaymentsDto;
    }

    @Override
    public  ResponseCurrencyDto updateCurrency() {
        ResponseCurrencyDto responseCurrencyDto;
        try {
            File file = new File("src/main/resources/directory/currency.json");
            responseCurrencyDto = new ObjectMapper().readValue(file, ResponseCurrencyDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseCurrencyDto;
    }
}