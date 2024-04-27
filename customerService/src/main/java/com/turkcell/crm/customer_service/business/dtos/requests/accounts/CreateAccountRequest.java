package com.turkcell.crm.customer_service.business.dtos.requests.accounts;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public record CreateAccountRequest (
    @NotNull
    @Size(min = 1)
     String status,
    @NotNull
    @Size(min = 1)
    String name,
    @NotNull
    @Size(min = 1)
    String number,
    int customerId,
    int accountTypeId,
    @Valid
    List<AccountAddressDto> accountAddresses
){}
