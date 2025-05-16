package com.inter.shipping_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "balance")
public class Balance {
    @Id
    private Long id;
    private Double balanceReal;
    private Double balanceDolar;
    private TypeBalance typeBalance;

}
