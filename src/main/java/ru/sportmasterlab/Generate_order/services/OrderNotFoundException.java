package ru.sportmasterlab.Generate_order.services;

public class OrderNotFoundException extends RuntimeException {

    private final String orderCode;

    public OrderNotFoundException(String orderCode) {
        this.orderCode = orderCode;
    }

    @Override
    public String getMessage() {
        return "Order with orderCode = " + orderCode + " not found";
    }
}
