package com.inter.shipping_service.controller;

import ch.qos.logback.core.boolex.EvaluationException;
import com.inter.shipping_service.dto.UserDto;
import com.inter.shipping_service.model.User;
import com.inter.shipping_service.service.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @GetMapping(value = "/{id}")
    public void register(@PathVariable long id){
        registerService.userById(id);
    }

    @PostMapping
    public void register(@Valid @RequestBody UserDto body){
        registerService.register(body);
    }
}