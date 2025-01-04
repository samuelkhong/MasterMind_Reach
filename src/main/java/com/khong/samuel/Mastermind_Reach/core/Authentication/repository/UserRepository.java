package com.khong.samuel.Mastermind_Reach.core.Authentication.repository;

import com.khong.samuel.Mastermind_Reach.core.Authentication.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    // Query method to find a user by their username
    Optional<User> findByUsername(String username);
}
