package com.inter.shipping_service.dto;

import org.springframework.format.annotation.NumberFormat;

public record BalanceResponseDto (@NumberFormat(pattern = "#0.00") Double balanceReal,
                                  @NumberFormat(pattern = "#0.00") Double balanceDollar){
}
