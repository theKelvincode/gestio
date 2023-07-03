package com.gestio.fms.auth.repository;

import com.gestio.fms.auth.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// this interface is responsible for communicating with the database.
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    // this method will return find user by email and construct the query for us
    Optional<User> findByEmail(String email);
}