package com.inter.shipping_service.controller;

import com.inter.shipping_service.dto.UserDto;
import com.inter.shipping_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/all/")
    public ResponseEntity<?> users(){
        var users = userService.getUsers();

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Users not found");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    @GetMapping(value = "/id")
    public ResponseEntity<?> getUserById(@RequestParam long id){
        if (!userService.existUserById(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
        var user = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping(value = "/documentNumber")
    public ResponseEntity<?> getBalanceByDocumentNumber(@RequestParam String documentNumber){
        if (!userService.existsUserByDocumentNumber(documentNumber)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
        var user = userService.getUserByDocumentNumber(documentNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping(value = "/create/")
    public ResponseEntity<?> postUser(@RequestBody @Valid UserDto userDto){
        if (userService.existsUserByDocumentNumber(userDto.documentNumber())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Document is already registered");
        }
        else if (userService.existUserByEmail(userDto.email())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail is already registered");
        }
        var user = userService.saveUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}