package com.khong.samuel.Mastermind_Reach.core.Authentication.security;

import com.khong.samuel.Mastermind_Reach.core.Authentication.model.CustomUserDetails;
import com.khong.samuel.Mastermind_Reach.core.Authentication.model.User;
import com.khong.samuel.Mastermind_Reach.core.Authentication.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository UserRepository;

    public UserDetailService(UserRepository UserRepository) {
        this.UserRepository = UserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user details from MongoDB
        User user = UserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Convert roles to GrantedAuthority
        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Return the CustomUserDetails object
        return new CustomUserDetails(user.getUsername(), user.getPassword(), authorities, user.getId());

    }

}
