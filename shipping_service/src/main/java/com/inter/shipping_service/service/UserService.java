package com.inter.shipping_service.service;

import com.inter.shipping_service.dto.UserDto;
import com.inter.shipping_service.model.TypeUser;
import com.inter.shipping_service.model.User;
import com.inter.shipping_service.repository.UserRepository;
import org.hibernate.type.SetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Buscar todos os usuários
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    // Buscar usuário por Id
    public User getUserById(@PathVariable long id){
        return userRepository.findById(id).orElse(null);
    }

    // Buscar saldo através do documento
    public User getBalanceByDocumentNumber(@PathVariable String documentNumber){
        return userRepository.findByDocumentNumber(documentNumber).orElse(null);
    }


    // Salvar usuário
    public ResponseEntity<String> saveUser(@RequestBody UserDto userDto){
        String document = userDto.documentNumber();
        TypeUser typeUser = null;

        if(document != null && document.length() == 11) {
            typeUser = TypeUser.PF;
        }
        else if(document != null && document.length() == 14) {
            typeUser = TypeUser.PJ;
        }
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Document Number must be 11 (CPF) or 14 (CNPJ) digits.");

        User user = new User(userDto);
        user.setType(typeUser);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Deletar usuário
    public String deleteUser(@PathVariable long id){
        userRepository.deleteById(id);
        return "success";
    }
}
