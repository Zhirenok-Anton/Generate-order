package ru.sportmasterlab.Generate_order.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.sportmasterlab.Generate_order.model.admin.ResponseCurrencyDto;
import ru.sportmasterlab.Generate_order.model.admin.ResponsePaymentsDto;
import ru.sportmasterlab.Generate_order.services.AdminService;

@RestController
@RequestMapping(value = "test/api/v1/admin/directory")
public class AdminOrderV1Controller {

    AdminService adminService;

    public AdminOrderV1Controller(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping(value = "/payments")
    @ResponseStatus(HttpStatus.OK)
    public ResponsePaymentsDto getDirectoryPayments(){
        return adminService.updateDirectoryPayments();
    }

    @GetMapping(value = "/currency")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCurrencyDto getDirectoryCurrency(){
        return adminService.updateDirectoryCurrency();
    }
}