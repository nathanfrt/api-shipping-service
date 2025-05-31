package com.inter.shipping_service.controller;

import com.inter.shipping_service.dto.UserDto;
import com.inter.shipping_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping("/user")
@Tag(name = "Usuário", description = "Controlador para cadastrar e consultar usuários")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/all/")
    @Operation(summary = "Obter lista de todos os usuários cadastrados")
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
    @Operation(summary = "Obter usuário cadastrado pelo ID")
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
    @Operation(summary = "Obter usuário cadastrado pelo Document Number (número de documento)")
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
    @Operation(summary = "Cadastrar usuário")
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