package ru.sportmasterlab.Generate_order.model.dataBase;

public record PaymentsDto(
        Long id,
        String name,
        String code,
        String idPaymentType,
        String idCreditProduct,
        Boolean isActual) {
}