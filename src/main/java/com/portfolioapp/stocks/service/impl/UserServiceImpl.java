package com.portfolioapp.stocks.service.impl;

import com.portfolioapp.stocks.model.User;
import com.portfolioapp.stocks.repository.UserRepo;
import com.portfolioapp.stocks.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    public void saveUser(User user)
    {
        userRepo.save(user);
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return userRepo.findById(userId);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
