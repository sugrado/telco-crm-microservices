package com.turkcell.crm.account_service.business.rules;

import com.turkcell.crm.account_service.api.clients.CustomerClient;
import com.turkcell.crm.account_service.api.clients.dtos.customers.CheckAddressAndCustomerMatchRequest;
import com.turkcell.crm.account_service.data_access.abstracts.AccountAddressRepository;
import com.turkcell.crm.account_service.data_access.abstracts.AccountRepository;
import com.turkcell.crm.account_service.entities.concretes.Account;
import com.turkcell.crm.common.exceptions.types.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountAddressBusinessRules {
    private final AccountAddressRepository accountAddressRepository;
    private final AccountRepository accountRepository;
    private final CustomerClient customerClient;

    public void addressMustBelongToAccountOwner(int accountId, int addressId) {
        Account account = accountRepository.findById(accountId).get();
        customerClient.checkAddressAndCustomerMatch(new CheckAddressAndCustomerMatchRequest(account.getCustomerId(), addressId));
    }

    public void addressShouldNotBeExistInAccount(int accountId, int addressId) {
        boolean exists = accountAddressRepository.existsByAccountIdAndAddressId(accountId, addressId);
        if (exists) {
            throw new BusinessException("Address is already exist in account");
        }
    }
}
