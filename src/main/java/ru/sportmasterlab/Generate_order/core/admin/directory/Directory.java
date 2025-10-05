package ru.sportmasterlab.Generate_order.core.admin.directory;

import ru.sportmasterlab.Generate_order.model.admin.CurrencyDto;
import ru.sportmasterlab.Generate_order.model.admin.PaymentsDto;

import java.util.ArrayList;

public class Directory {
    public static ArrayList<PaymentsDto> paymentsDirectory = new ArrayList<>();
    public static ArrayList<CurrencyDto> currencyDirectory = new ArrayList<>();

    public static void updateCurrencyDirectory (ArrayList<CurrencyDto> listCurrency){
        currencyDirectory.clear();
        currencyDirectory.addAll(listCurrency);
    }

    public static void updatePaymentsDirectory (ArrayList<PaymentsDto> listPayments){
        paymentsDirectory.clear();
        paymentsDirectory.addAll(listPayments);
    }
}