package com.inter.shipping_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String phone;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String documentNumber;

    @Enumerated(EnumType.STRING)
    private TypeUser type;

    private Double balanceReal;
    private Double balanceDolar;
}
