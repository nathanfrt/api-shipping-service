package com.inter.shipping_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BalanceDto(@NotBlank String documentNumber,
                         @NotNull Double amount) {
}
