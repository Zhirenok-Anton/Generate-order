package ru.sportmasterlab.Generate_order.services;

import ru.sportmasterlab.Generate_order.model.dataBase.CurrencyDto;
import ru.sportmasterlab.Generate_order.model.dataBase.PaymentsDto;

import java.util.ArrayList;
import java.util.Optional;

public interface AdminService {

    Optional<ArrayList<PaymentsDto>> updateDirectoryPayments();
    Optional<ArrayList<CurrencyDto>> updateDirectoryCurrency();
}