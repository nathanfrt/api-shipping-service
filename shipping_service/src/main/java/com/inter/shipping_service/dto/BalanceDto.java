package com.inter.shipping_service.dto;

import com.inter.shipping_service.model.TypeBalance;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record BalanceDto(@NotNull String documentNumber,
                         @NotNull Double amount,
                         @NotNull @Enumerated(EnumType.STRING) TypeBalance typeBalance) {
}
