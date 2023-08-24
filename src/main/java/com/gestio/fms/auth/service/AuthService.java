package com.gestio.fms.auth.service;

import com.gestio.fms.auth.model.AuthRequest;
import com.gestio.fms.auth.model.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    public AuthResponse authenticate(AuthRequest request) {
        return null;
    }
}