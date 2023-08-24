package com.gestio.fms.registration.token;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


// here, we expose the ability for us to save a confirmation token
@Service
public class ConfirmationTokenService {


    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }


    //
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }
    //
    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

   // method set when token was confirmed
    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }
}
