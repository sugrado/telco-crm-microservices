package com.turkcell.crm.account_service.business.dtos.responses.account_types;

import java.time.LocalDateTime;

public record GetAllAccountTypeResponse(
        int id,
        LocalDateTime createdDate,
        String name
) {
}
