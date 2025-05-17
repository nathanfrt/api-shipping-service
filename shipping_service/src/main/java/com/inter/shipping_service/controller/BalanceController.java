package com.inter.shipping_service.controller;

import com.inter.shipping_service.dto.BalanceDto;
import com.inter.shipping_service.model.Balance;
import com.inter.shipping_service.model.User;
import com.inter.shipping_service.repository.BalanceRepository;
import com.inter.shipping_service.repository.UserRepository;
import com.inter.shipping_service.service.BalanceService;
import com.inter.shipping_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;
    private UserService userService;

    @GetMapping("/{documentNumber}")
    public void getBalancebyDocumentNumber(@RequestParam String documentNumber) {
        Optional<User> user = Optional.ofNullable(userService.getBalanceByDocumentNumber(documentNumber));
        if (user.isPresent()) {
            userService.getBalanceByDocumentNumber(documentNumber);
        }
    }

}
