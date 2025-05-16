package com.inter.shipping_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Password is required")
    private String password;

    private String phone;

    @Email(message = "Invalid e-mail")
    private String email;

    @NotBlank(message = "Document Number is required")
    private String documentNumber;
}
