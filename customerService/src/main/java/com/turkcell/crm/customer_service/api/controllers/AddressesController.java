package com.turkcell.crm.customer_service.api.controllers;


import com.turkcell.crm.common.shared.dtos.customers.CheckAddressAndCustomerMatchRequest;
import com.turkcell.crm.common.shared.dtos.customers.GetValidatedCustomerAddressesListItemDto;
import com.turkcell.crm.common.shared.dtos.customers.GetValidatedCustomerAddressesRequest;
import com.turkcell.crm.customer_service.business.abstracts.AddressService;
import com.turkcell.crm.customer_service.business.dtos.requests.addresses.ChangeDefaultAddressRequest;
import com.turkcell.crm.customer_service.business.dtos.requests.addresses.CreateAddressRequest;
import com.turkcell.crm.customer_service.business.dtos.requests.addresses.UpdateAddressRequest;
import com.turkcell.crm.customer_service.business.dtos.responses.addresses.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("customer-service/api/v1/addresses")
public class AddressesController {
    private final AddressService addressService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedAddressResponse add(@Valid @RequestBody CreateAddressRequest createAddressRequest) {
        return this.addressService.add(createAddressRequest);
    }

    @PatchMapping("{id}")
    public UpdatedAddressResponse update(@PathVariable int id, @Valid @RequestBody UpdateAddressRequest updateAddressRequest) {
        return this.addressService.update(id, updateAddressRequest);
    }

    @PatchMapping("default")
    public ChangedDefaultAddressResponse changeDefaultAddress(@RequestBody ChangeDefaultAddressRequest changeDefaultAddressRequest) {
        return this.addressService.changeDefaultAddress(changeDefaultAddressRequest);
    }

    @DeleteMapping("{id}")
    public DeletedAddressResponse delete(@PathVariable int id) {
        return this.addressService.delete(id);
    }

    @PostMapping("check-address-and-customer-match")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void checkAddressAndCustomerMatch(@RequestBody @Valid CheckAddressAndCustomerMatchRequest checkAddressAndCustomerMatchRequest) {
        this.addressService.checkAddressAndCustomerMatch(checkAddressAndCustomerMatchRequest);
    }

    @GetMapping("check-if-address-exists/{addressId}")
    public void checkIfAddressExists(@PathVariable int addressId) {
        this.addressService.checkIfAddressExists(addressId);
    }

    @GetMapping("{id}")
    public GetByIdAddressResponse getById(@PathVariable int id) {
        return this.addressService.getById(id);
    }

    @PostMapping("validated-customer-addresses")
    public List<GetValidatedCustomerAddressesListItemDto> getValidatedCustomerAddresses(@RequestBody @Valid GetValidatedCustomerAddressesRequest getValidatedCustomerAddressesRequest) {
        return this.addressService.getAllByCustomerAndIds(getValidatedCustomerAddressesRequest);
    }
}
