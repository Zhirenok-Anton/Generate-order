package ru.sportmasterlab.Generate_order.services;

import ru.sportmasterlab.Generate_order.model.admin.ResponseCurrencyDto;
import ru.sportmasterlab.Generate_order.model.admin.ResponsePaymentsDto;

public interface AdminService {

    ResponsePaymentsDto updateDirectoryPayments();
    ResponseCurrencyDto updateDirectoryCurrency();
}