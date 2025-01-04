package com.khong.samuel.Mastermind_Reach.core.Authentication.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Builder
@Document
@Data  // Lombok annotation to generate getters, setters, toString, equals, and hashCode
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private List<String> roles;  // List of roles assigned to the user
}
