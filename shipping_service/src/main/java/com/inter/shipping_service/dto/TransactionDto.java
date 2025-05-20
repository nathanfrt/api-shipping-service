package com.inter.shipping_service.dto;

import jakarta.validation.constraints.NotBlank;

public record TransactionDto(
        @NotBlank(message = "Amount is amount") Double amount,
        @NotBlank(message = "transactionBy is required") String transactionBy,
        @NotBlank(message = "transactionTo is required") String transactionTo) {}
