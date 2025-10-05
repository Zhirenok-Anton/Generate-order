package ru.sportmasterlab.Generate_order.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.sportmasterlab.Generate_order.model.order.OrderResponseDto;
import ru.sportmasterlab.Generate_order.model.order.created.OrderRequestDto;
import ru.sportmasterlab.Generate_order.services.OrderService;

@RestController
@RequestMapping(value = "test/api/v1/order")
public class GenerateOrderV1Controller {
    private final OrderService orderService;

    public GenerateOrderV1Controller(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/{orderId:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponseDto getOrder(@PathVariable Long orderId){
        return orderService.getorder(orderId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto createOrder(@Valid @RequestBody OrderRequestDto request) {
        Long orderCode = orderService.createOrder(request);
        return orderService.getorder(orderCode);
    }
}
