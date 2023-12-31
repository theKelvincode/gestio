package com.gestio.fms.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
// help build our object easily using design pattern builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "gestio_user")
@SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)

// implements the user details for spring security...
public  class User implements UserDetails {

    public User(String email,
                String password,
                String firstName,
                String lastName,
                UserRole role,
                int phoneNumber) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.phoneNumber = phoneNumber;
    }

    @Id
    @GeneratedValue(generator = "user_sequence", strategy =  GenerationType.SEQUENCE)
    private Long id;
    private  String email;
    private String password;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    //set app user to locked by default
    private Boolean locked = false;

    private Boolean enabled = false;
    private int phoneNumber;



    // this method should return a list of roles
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // based on our design, a user can only have one role.
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
//                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}