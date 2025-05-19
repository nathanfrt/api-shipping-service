package com.inter.shipping_service.dto;

import jakarta.validation.constraints.NotBlank;

public record ExchangeDto(@NotBlank(message = "Amount is amount") Double amount,
                          @NotBlank(message = "Document Number is required") String documentNumber) {}
