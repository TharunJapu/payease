package com.payease.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payease.app.dao.UserRepository;
import com.payease.app.model.User;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public void register(User user) {
        userRepository.save(user);
    }
}