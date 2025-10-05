package ru.sportmasterlab.Generate_order.model.admin;

import java.util.ArrayList;

public record ResponseCurrencyDto(
        ArrayList<CurrencyDto> currency) {
}
