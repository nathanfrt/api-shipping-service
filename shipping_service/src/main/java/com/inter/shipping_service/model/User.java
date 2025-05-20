package com.inter.shipping_service.model;

import com.inter.shipping_service.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.NumberFormat;

import java.lang.reflect.Type;

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

    @NumberFormat(pattern = "#0.00")
    private Double balanceReal = 0.00;

    @NumberFormat(pattern = "#0.00")
    private Double balanceDollar = 0.00;

    public User(UserDto user) {
        BeanUtils.copyProperties(user, this);
    }

}


