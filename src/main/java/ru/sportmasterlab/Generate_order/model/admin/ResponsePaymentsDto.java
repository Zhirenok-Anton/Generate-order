package ru.sportmasterlab.Generate_order.model.admin;

import java.util.ArrayList;

public record ResponsePaymentsDto (
        ArrayList<PaymentsDto> payments){
}
