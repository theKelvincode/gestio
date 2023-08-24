package com.gestio.fms.security.config;

import com.gestio.fms.auth.JwtAuthFilter;
import com.gestio.fms.auth.service.UserService;
import com.gestio.fms.user.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;

    public WebSecurityConfig(JwtAuthFilter jwtAuthFilter, BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers("api/tenant/**").hasRole(UserRole.TENANT.name())
                        .requestMatchers("api/staff/**").hasRole(UserRole.STAFF.name())
                        .requestMatchers("api/admin/**").hasRole(UserRole.ADMIN.name())
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // the data access object responsible for fetching the user details and encode password, etc
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // user details
        authProvider.setUserDetailsService(userService);
        // password encoder
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

}