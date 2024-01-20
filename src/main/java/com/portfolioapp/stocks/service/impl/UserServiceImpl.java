package com.portfolioapp.stocks.service.impl;

import com.portfolioapp.stocks.model.User;
import com.portfolioapp.stocks.repository.UserRepo;
import com.portfolioapp.stocks.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    public User saveUser(User user)
    {
        return userRepo.save(user);
    }
}
