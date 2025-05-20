package com.inter.shipping_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionDto(
        @NotNull(message = "Amount is amount") Double amount,
        @NotBlank(message = "transactionBy is required") String transactionBy,
        @NotBlank(message = "transactionTo is required") String transactionTo) {}
