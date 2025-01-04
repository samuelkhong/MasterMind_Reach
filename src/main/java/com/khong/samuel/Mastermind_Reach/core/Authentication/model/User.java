package com.khong.samuel.Mastermind_Reach.core.Authentication.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Data  // Lombok annotation to generate getters, setters, toString, equals, and hashCode
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private List<String> roles;  // List of roles assigned to the user

    public User() {
        this.roles = new ArrayList<>();  // Initialize roles to prevent null values
    }

    public User(String username, String password, String email, List<String> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

}
