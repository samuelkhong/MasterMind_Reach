package com.khong.samuel.Mastermind_Reach.core.Authentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailService userDetailService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt for password encoding
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers ("/login", "/css/**", "/js/**", "/register").permitAll()  // Allow unauthenticated access to login page and static files
                .anyRequest().authenticated()  // Require authentication for all other requests
                .and()
                .formLogin()  // Enable form-based login
                .permitAll()  // Allow everyone to access the login page
                .and()
                .logout()  // Enable logout functionality
                .logoutUrl("/logout")  // The default logout URL is "/logout"
                .logoutSuccessUrl("/login?logout")  // Redirect to login page after successful logout
                .invalidateHttpSession(true)  // Invalidate the session to clear any user session data
                .clearAuthentication(true)  // Clear authentication data
                .deleteCookies("JSESSIONID")
                .and()
                .httpBasic();  // Optionally enable basic HTTP authentication (just in case)

        return http.build();
    }

    //potentially add another security filter to prevent logged in users from registering

//    @Bean
//    public SecurityFilterChain redirectAuthenticatedToHome(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .requestMatchers("/register")
//                .authenticated()
//                .and()
//                .exceptionHandling()
//                .accessDeniedPage("/");  // Redirect authenticated users to /home (or dashboard)
//
//        return http.build();
//    }



    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // Configure AuthenticationManager to use my customUserService and PasswordEncoder
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder());  // Use the PasswordEncoder

        return authenticationManagerBuilder.build();
    }
}