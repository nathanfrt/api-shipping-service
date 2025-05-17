package com.inter.shipping_service.model;

import com.inter.shipping_service.dto.BalanceDto;
import com.inter.shipping_service.dto.UserDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "balance")
public class Balance {
    @Id
    private Long id;
    private Double balance;
    private Double quotation;
    private TypeBalance typeBalance;
    private Date updatedAt;
    private String documentNumber;

    @ManyToOne
    private User user;

    public Balance(BalanceDto balance){
        BeanUtils.copyProperties(balance, this);
    }
}
