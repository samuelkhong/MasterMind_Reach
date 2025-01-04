package com.khong.samuel.Mastermind_Reach.core.Authentication.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
@ToString
public class CustomUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String userId;

    @Override
    public boolean isAccountNonExpired() {
        return true; // Update as needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Update as needed
    }



    @Override
    public boolean isEnabled() {
        return true; // Update as needed
    }

}

