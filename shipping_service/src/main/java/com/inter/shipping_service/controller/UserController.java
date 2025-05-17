package com.inter.shipping_service.controller;

import com.inter.shipping_service.dto.UserDto;
import com.inter.shipping_service.model.User;
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

    @GetMapping(value = "/{id}")
    public void user(@PathVariable long id){
        userService.getUserById(id);
    }

    @GetMapping
    public void users(){
        userService.getUsers();
    }

    @PostMapping()
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto){
        userService.saveUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}