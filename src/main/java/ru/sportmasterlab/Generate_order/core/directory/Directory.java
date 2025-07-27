package ru.sportmasterlab.Generate_order.core.directory;

import ru.sportmasterlab.Generate_order.model.dataBase.CurrencyDto;
import ru.sportmasterlab.Generate_order.model.dataBase.PaymentsDto;

import java.util.ArrayList;
import java.util.Optional;

public class Directory {
    public static ArrayList<PaymentsDto> paymentsDirectory = new ArrayList<>();
    public static ArrayList<CurrencyDto> currencyDirectory = new ArrayList<>();

    public static void updateCurrencyDirectory (Optional<ArrayList<CurrencyDto>> listCurrency){
        currencyDirectory.clear();
        listCurrency.ifPresent(currencyDto -> currencyDirectory.addAll(currencyDto));
    }

    public static void updatePaymentsDirectory (Optional<ArrayList<PaymentsDto>> listPayments){
        paymentsDirectory.clear();
        listPayments.ifPresent(paymentsDto -> paymentsDirectory.addAll(paymentsDto));
    }
}