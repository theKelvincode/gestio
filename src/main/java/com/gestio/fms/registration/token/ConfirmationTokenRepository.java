package com.gestio.fms.registration.token;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

        // we need a find-by confirmation token method
        Optional<ConfirmationToken> findByToken(String token);

        @Transactional
        @Modifying
        @Query("UPDATE ConfirmationToken c " +
                "SET c.confirmedAt = ?2 " +
                "WHERE c.token = ?1")
        int updateConfirmedAt(String token,
                              LocalDateTime confirmedAt);
}