package ru.sportmasterlab.Generate_order.repository;

import ru.sportmasterlab.Generate_order.model.dataBase.CurrencyDto;
import ru.sportmasterlab.Generate_order.model.dataBase.PaymentsDto;

import java.util.ArrayList;
import java.util.Optional;

interface AdminRepository {

    Optional<ArrayList<PaymentsDto>> updatePayments();
    Optional<ArrayList<CurrencyDto>> updateCurrency();
}
