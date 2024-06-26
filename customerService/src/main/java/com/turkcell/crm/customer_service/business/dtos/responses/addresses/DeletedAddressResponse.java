package com.turkcell.crm.customer_service.business.dtos.responses.addresses;

import java.time.LocalDateTime;

public record DeletedAddressResponse(
        int id,
        LocalDateTime deletedDate,
        String street,
        String houseFlatNumber,
        String description,
        boolean defaultAddress,
        int cityId,
        int districtId
) {
}
