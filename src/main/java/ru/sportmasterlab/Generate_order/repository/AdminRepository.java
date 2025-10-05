package ru.sportmasterlab.Generate_order.repository;

import ru.sportmasterlab.Generate_order.model.admin.ResponseCurrencyDto;
import ru.sportmasterlab.Generate_order.model.admin.ResponsePaymentsDto;

interface AdminRepository {

    ResponsePaymentsDto updatePayments();
    ResponseCurrencyDto updateCurrency();
}
