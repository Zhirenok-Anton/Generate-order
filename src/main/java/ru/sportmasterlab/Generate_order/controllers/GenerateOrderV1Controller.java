package ru.sportmasterlab.Generate_order.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.sportmasterlab.Generate_order.model.OrderDto;
import ru.sportmasterlab.Generate_order.model.Created.OrderRequest;
import ru.sportmasterlab.Generate_order.services.OrderService;

@RestController
@RequestMapping(value = "/generate/order/api/v1")
public class GenerateOrderV1Controller {
    private final OrderService orderService;

    public GenerateOrderV1Controller(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/{orderId:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto getOrder(@PathVariable Long orderId){
        return orderService.getorder(orderId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@Valid @RequestBody OrderRequest request) {
        Long orderCode = orderService.createOrder(request);
        return orderService.getorder(orderCode);
    }
}
