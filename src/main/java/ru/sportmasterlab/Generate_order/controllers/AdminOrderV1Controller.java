package ru.sportmasterlab.Generate_order.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.sportmasterlab.Generate_order.model.dataBase.CurrencyDto;
import ru.sportmasterlab.Generate_order.model.dataBase.PaymentsDto;
import ru.sportmasterlab.Generate_order.services.AdminService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping(value = "/admin/api/v1/directory")
public class AdminOrderV1Controller {

    AdminService adminService;

    public AdminOrderV1Controller(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping(value = "/payments")
    @ResponseStatus(HttpStatus.OK)
    public Optional<ArrayList<PaymentsDto>> getDirectoryPayments(){
        return adminService.updateDirectoryPayments();
    }

    @GetMapping(value = "/currency")
    @ResponseStatus(HttpStatus.OK)
    public Optional<ArrayList<CurrencyDto>> getDirectoryCurrency(){
        return adminService.updateDirectoryCurrency();
    }
}