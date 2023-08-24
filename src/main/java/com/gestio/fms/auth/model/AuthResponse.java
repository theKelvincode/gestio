package com.gestio.fms.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class AuthResponse {

    // token that will be sent back to the user
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}
