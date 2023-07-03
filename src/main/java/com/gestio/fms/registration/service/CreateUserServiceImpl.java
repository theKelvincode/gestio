package com.gestio.fms.registration.service;

import com.gestio.fms.auth.repository.UserRepository;
import com.gestio.fms.registration.model.NewUser;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateUserServiceImpl {

    public String createUser(NewUser request) {
        return "works";
    }
}