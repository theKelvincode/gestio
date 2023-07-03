package com.gestio.fms.registration.model;

import com.gestio.fms.auth.entities.UserRole;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class NewUser {

    private String firstName;
    private  String lastName;
    private  String email;
    private  String password;
    private UserRole role;
}
