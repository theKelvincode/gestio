package com.gestio.fms.auth.controller;

import com.gestio.fms.auth.model.AuthRequest;
import com.gestio.fms.auth.model.AuthResponse;
import com.gestio.fms.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthResponse> authenticate (@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
