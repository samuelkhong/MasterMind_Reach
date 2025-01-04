package com.khong.samuel.Mastermind_Reach.core.Authentication.service;

import com.khong.samuel.Mastermind_Reach.core.Authentication.model.User;
import com.khong.samuel.Mastermind_Reach.core.Authentication.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService( UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public User save(User user) {

        // Save the user to the database
        return userRepository.save(user);
    }




}
