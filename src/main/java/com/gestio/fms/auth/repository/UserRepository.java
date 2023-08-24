package com.gestio.fms.auth.repository;

import com.gestio.fms.user.User;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// this interface is responsible for communicating with the database.
@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    // this method will return find user by email and construct the query for us
    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(
            "UPDATE User u " +
            "SET u.enabled = TRUE " +
                    "WHERE u.email = ?1")
    int enableUser(String email);
}