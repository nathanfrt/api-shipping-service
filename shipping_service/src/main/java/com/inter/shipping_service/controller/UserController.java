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
        try {
            var users = userService.getUsers();

            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Users not found");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(users);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value = "/id")
    public ResponseEntity<?> getUserById(@RequestParam long id){
        try {
            if (!userService.existUserById(id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }
            var user = userService.getUserById(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value = "/documentNumber")
    public ResponseEntity<?> getBalanceByDocumentNumber(@RequestParam String documentNumber){
        try {
            if (!userService.existsUserByDocumentNumber(documentNumber)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }
            var user = userService.getUserByDocumentNumber(documentNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(value = "/create/")
    public ResponseEntity<?> postUser(@RequestBody @Valid UserDto userDto){
        try {
            if (userService.existsUserByDocumentNumber(userDto.documentNumber())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Document is already registered");
            } else if (userService.existUserByEmail(userDto.email())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail is already registered");
            }
            var user = userService.saveUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}