package com.inter.shipping_service.service;

import com.inter.shipping_service.dto.UserDto;
import com.inter.shipping_service.exception.InvalidDocument;
import com.inter.shipping_service.exception.NotExist;
import com.inter.shipping_service.model.BalanceResponse;
import com.inter.shipping_service.model.TypeUser;
import com.inter.shipping_service.model.User;
import com.inter.shipping_service.repository.UserRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public User getUserById(long id){
        return userRepository.findById(id).orElse(null);
    }

    // Buscar usuário por documento
    public User getUserByDocumentNumber(String documentNumber){
        return userRepository.findUserByDocumentNumber(documentNumber);
    }

    // Salvar usuário
    public User saveUser(UserDto userDto){
        String document = userDto.documentNumber();

        if (document == null)
            throw new InvalidDocument("Invalid document number");;

        document = removeNonNumericCharacters(document);
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

    // Verifica se existe usuário para det. email
    public Boolean existUserByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    // Busca saldos para det. documento
    public BalanceResponse getBalanceByDocumentNumber(String documentNumber){
        exceptionDocumentNumber(documentNumber);
        return userRepository.findBalanceByDocumentNumber(documentNumber);
    }

    public Double getBalanceRealByDocumentNumber(String documentNumber){
        exceptionDocumentNumber(documentNumber);
        return userRepository.findBalanceRealByDocumentNumber(documentNumber);
    }

    public Double getBalanceDolarByDocumentNumber(String documentNumber){
        exceptionDocumentNumber(documentNumber);
        return userRepository.findBalanceDolarByDocumentNumber(documentNumber);
    }

    // Exibe exception caso não exista usuário para det. documento
    public void exceptionDocumentNumber(String documentNumber){
        if (!userRepository.existsByDocumentNumber(documentNumber)){
            throw new NotExist("User not found");
        }
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public String removeNonNumericCharacters(String text) {
        if (text == null) { return null; }
        return text.replaceAll("[^0-9]", "");
    }

}
