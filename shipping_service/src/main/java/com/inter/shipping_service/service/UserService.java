package com.inter.shipping_service.service;

import com.inter.shipping_service.dto.UserDto;
import com.inter.shipping_service.exception.InvalidDocument;
import com.inter.shipping_service.model.BalanceResponse;
import com.inter.shipping_service.model.TypeUser;
import com.inter.shipping_service.model.User;
import com.inter.shipping_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Buscar usuário por documento
    public User getUserByDocumentNumber(@PathVariable String documentNumber){
        return userRepository.findUserByDocumentNumber(documentNumber);
    }

    // Salvar usuário
    public User saveUser(@RequestBody UserDto userDto){
        String document = userDto.documentNumber();

        if (document == null)
            throw new InvalidDocument("Invalid document number");;

        document = userRepository.removenonNumericCharacters(document);
        TypeUser typeUser = null;

        if(document.length() == 11) {
            typeUser = TypeUser.PF;
        }
        else if(document.length() == 14) {
            typeUser = TypeUser.PJ;
        }
        else throw new InvalidDocument("Invalid document number");

        User user = new User(userDto);
        user.setType(typeUser);

        return userRepository.save(user);
    }

    // Verifica se existe usuário para det. documento
    public Boolean existsUserByDocumentNumber(String documentNumber){
        return userRepository.existsByDocumentNumber(documentNumber);
    }

    // Verifica se existe usuário para det. id
    public Boolean existUserById(long id){
        return userRepository.existsById(id);
    }

    // Verifica se existe usuário para det. id
    public Boolean existUserByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    // Busca saldos para det. documento
    public BalanceResponse getBalanceByDocumentNumber(String documentNumber){
        return userRepository.findBalanceByDocumentNumber(documentNumber);
    }

}
