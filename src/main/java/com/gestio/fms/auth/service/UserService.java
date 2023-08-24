package com.gestio.fms.auth.service;

import com.gestio.fms.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// this is how we find users once we try to log in

@Service
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
        "user with email %s not found";


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /* We have to figure out how to find a user by username, which in this case will be the email.
        To achieve that, what we need is to have a query against our database, hence a reference to the application
        repository interface */
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow( () ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public int enableUser(String email) {
       return userRepository.enableUser(email);
    }
}
