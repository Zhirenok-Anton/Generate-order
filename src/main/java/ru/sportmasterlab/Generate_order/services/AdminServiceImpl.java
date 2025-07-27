package ru.sportmasterlab.Generate_order.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.sportmasterlab.Generate_order.core.directory.Directory;
import ru.sportmasterlab.Generate_order.model.dataBase.CurrencyDto;
import ru.sportmasterlab.Generate_order.model.dataBase.PaymentsDto;
import ru.sportmasterlab.Generate_order.repository.AdminRepositoryImpl;

import java.util.ArrayList;
import java.util.Optional;

@Primary
@Service
public class AdminServiceImpl implements AdminService{

    private final AdminRepositoryImpl adminRepository;

    public AdminServiceImpl(AdminRepositoryImpl adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Optional<ArrayList<PaymentsDto>> updateDirectoryPayments() {
        Optional<ArrayList<PaymentsDto>> listPayments = adminRepository.updatePayments();
        Directory.updatePaymentsDirectory(listPayments);
        return listPayments;
    }

    @Override
    public Optional<ArrayList<CurrencyDto>> updateDirectoryCurrency() {
        Optional<ArrayList<CurrencyDto>> listCurrency = adminRepository.updateCurrency();
        Directory.updateCurrencyDirectory(listCurrency);
        return listCurrency;
    }
}