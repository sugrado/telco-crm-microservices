package com.turkcell.crm.customer_service.business.rules;

import com.turkcell.crm.customer_service.business.constants.messages.Messages;
import com.turkcell.crm.customer_service.core.business.abstracts.MessageService;
import com.turkcell.crm.customer_service.core.utilities.exceptions.types.BusinessException;
import com.turkcell.crm.customer_service.data_access.abstracts.AddressRepository;
import com.turkcell.crm.customer_service.entities.concretes.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressBusinessRules {
    private final MessageService messageService;
    private final AddressRepository addressRepository;

    public void addressShouldBeExist(int id) {
        boolean exists = this.addressRepository.existsById(id);
        if (!exists) {
            throw new BusinessException(this.messageService.getMessage(Messages.AddressMessages.NOT_FOUND));
        }
    }

    public void addressShouldBeExist(Optional<Address> address) {
        if (address.isEmpty()) {
            throw new BusinessException(this.messageService.getMessage(Messages.AddressMessages.NOT_FOUND));
        }
    }

    public void addressAndCustomerShouldBeMatch(int addressId,int customerId) {
        Optional<Address> address = addressRepository.findByIdAndCustomerId(addressId, customerId);
        if (address.isEmpty()) {
            throw new BusinessException(this.messageService.getMessage(Messages.AddressMessages.CUSTOMER_ADDRESS_MISMATCH));
        }
    }
}