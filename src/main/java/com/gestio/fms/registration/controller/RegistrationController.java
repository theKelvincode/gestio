package com.gestio.fms.registration.controller;

import com.gestio.fms.auth.model.AuthResponse;
import com.gestio.fms.registration.model.NewUser;
import com.gestio.fms.registration.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/admin/v1/registration/")
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping("createUser.do")
    public ResponseEntity<String> createUser(@RequestBody NewUser request) {
        return ResponseEntity.ok(registrationService.createUser(request));
    }
}