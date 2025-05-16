package com.inter.shipping_service.dto;

import jakarta.validation.constraints.NotNull;

public record BalanceDto(@NotNull Long id, String typeBalance, @NotNull Double balance) {
}
