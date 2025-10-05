package ru.sportmasterlab.Generate_order.model.admin;

public record PaymentsDto(
        Long id,
        String name,
        String code,
        String idPaymentType,
        String idCreditProduct,
        Boolean isActual) {
}