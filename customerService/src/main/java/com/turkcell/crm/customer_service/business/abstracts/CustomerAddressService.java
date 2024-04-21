package com.turkcell.crm.customer_service.business.abstracts;

import com.turkcell.crm.customer_service.business.dtos.requests.customers.CustomerAddressDto;
import com.turkcell.crm.customer_service.business.dtos.requests.customers.CustomerContactDto;

import java.util.List;

public interface CustomerAddressService {
    void add(List<CustomerAddressDto> customerAddressDtoList);
}