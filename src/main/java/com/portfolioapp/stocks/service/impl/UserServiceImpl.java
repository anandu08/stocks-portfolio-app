package com.portfolioapp.stocks.service.impl;

import com.portfolioapp.stocks.exception.UserNotFoundException;
import com.portfolioapp.stocks.model.User;
import com.portfolioapp.stocks.repository.UserRepo;
import com.portfolioapp.stocks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    public void saveUser(User user)
    {
        userRepo.save(user);
    }

    @Override
    public Optional<User> findUserById(Long userId) {


        Optional<User> user= userRepo.findById(userId);
        if(user.isEmpty())
            throw new UserNotFoundException("No user found for userId:"+userId);

        return user;
    }

    @Override
    public User findUserByEmail(String email) {


        return userRepo.findByEmail(email);

    }
}
