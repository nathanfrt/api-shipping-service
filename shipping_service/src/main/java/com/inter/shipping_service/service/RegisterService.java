package com.inter.shipping_service.service;

import com.inter.shipping_service.model.User;
import com.inter.shipping_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepository;

    public void register(@RequestBody User body){
        userRepository.save(body);
    }

    public void userById(long id){
        userRepository.findById(id);
    }
}
