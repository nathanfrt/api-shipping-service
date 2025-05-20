package com.inter.shipping_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inter.shipping_service.model.TypeUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDto (
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Password is required") String password,
        @Email(message = "Invalid e-mail") String email,
        @NotBlank (message = "Document Number is required") String documentNumber,
        @JsonIgnore TypeUser typeUser,
        @JsonIgnore Double balanceReal,
        @JsonIgnore Double balanceDollar) {}



