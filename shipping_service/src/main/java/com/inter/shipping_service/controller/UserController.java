package com.inter.shipping_service.controller;

import com.inter.shipping_service.dto.UserDto;
import com.inter.shipping_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping("/user")
@Tag(name = "Usuário", description = "Controlador para cadastrar e consultar usuários")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping(value = "/all/")
    @Operation(summary = "Obter lista de todos os usuários cadastrados")
    public ResponseEntity<?> users(){
        try {
            var users = userService.getUsers();

            if (users.isEmpty()) {
                logger.warn("Users not found");
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(users);
        }
        catch (Exception e){
            logger.error("Error fetching users: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/id")
    @Operation(summary = "Obter usuário cadastrado pelo ID")
    public ResponseEntity<?> getUserById(@RequestParam long id){
        try {
            if (!userService.existUserById(id)) {
                logger.warn("User not found");
                return ResponseEntity.notFound().build();
            }
            var user = userService.getUserById(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
        catch (Exception e){
            logger.error("Error fetching users: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/documentNumber")
    @Operation(summary = "Obter usuário cadastrado pelo Document Number (número de documento)")
    public ResponseEntity<?> getBalanceByDocumentNumber(@RequestParam String documentNumber){
        try {
            if (!userService.existsUserByDocumentNumber(documentNumber)) {
                logger.warn("User not found");
                return ResponseEntity.notFound().build();
            }
            var user = userService.getUserByDocumentNumber(documentNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
        catch (Exception e){
            logger.error("Error fetching users: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/create/")
    @Operation(summary = "Cadastrar usuário")
    public ResponseEntity<?> postUser(@RequestBody @Valid UserDto userDto){
        try {
            if (userService.existsUserByDocumentNumber(userDto.documentNumber())) {
                logger.warn("User already registered");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already registered");
            } else if (userService.existUserByEmail(userDto.email())) {
                logger.warn("E-mail is already registered");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail is already registered");
            }
            var user = userService.saveUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
        catch (Exception e){
            logger.error("Error creating user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}