package com.inter.shipping_service.model;

import com.inter.shipping_service.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String password;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String documentNumber;

    @Enumerated(EnumType.STRING)
    private TypeUser type;

    private Double balanceReal = 0.00;
    private Double balanceDollar = 0.00;

    public User(UserDto user) {
        BeanUtils.copyProperties(user, this);
    }
}


