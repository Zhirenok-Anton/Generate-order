package ru.sportmasterlab.Generate_order.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.sportmasterlab.Generate_order.core.admin.directory.Directory;
import ru.sportmasterlab.Generate_order.model.admin.ResponseCurrencyDto;
import ru.sportmasterlab.Generate_order.model.admin.ResponsePaymentsDto;
import ru.sportmasterlab.Generate_order.repository.AdminRepositoryImpl;

@Primary
@Service
public class AdminServiceImpl implements AdminService{

    private final AdminRepositoryImpl adminRepository;

    public AdminServiceImpl(AdminRepositoryImpl adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public ResponsePaymentsDto updateDirectoryPayments() {
        ResponsePaymentsDto responsePaymentsDto = adminRepository.updatePayments();
        Directory.updatePaymentsDirectory(responsePaymentsDto.payments());
        return responsePaymentsDto;
    }

    @Override
    public ResponseCurrencyDto updateDirectoryCurrency() {
        ResponseCurrencyDto responseCurrencyDto = adminRepository.updateCurrency();
        Directory.updateCurrencyDirectory(responseCurrencyDto.currency());
        return responseCurrencyDto;
    }
}