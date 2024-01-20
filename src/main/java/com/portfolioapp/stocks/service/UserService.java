package com.portfolioapp.stocks.service;

import com.portfolioapp.stocks.model.User;

import java.util.Optional;

public interface UserService {


    void saveUser(User user);
    Optional<User> findUserById(Long userId);

    User findUserByEmail(String email);
}
