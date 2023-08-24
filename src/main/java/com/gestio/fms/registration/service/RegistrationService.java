package com.gestio.fms.registration.service;

import com.gestio.fms.auth.repository.UserRepository;
import com.gestio.fms.auth.service.JwtService;
import com.gestio.fms.auth.service.UserService;
import com.gestio.fms.email.EmailSender;
import com.gestio.fms.registration.model.NewUser;
import com.gestio.fms.registration.token.ConfirmationToken;
import com.gestio.fms.registration.token.ConfirmationTokenService;
import com.gestio.fms.user.User;
import com.gestio.fms.user.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserService userService;
    private final EmailSender emailSender;

    public RegistrationService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtService jwtService, ConfirmationTokenService confirmationTokenService, UserService userService, EmailSender emailSender) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
        this.confirmationTokenService = confirmationTokenService;
        this.userService = userService;
        this.emailSender = emailSender;
    }

    // this method will allow us to create a user, save it to the database and return the generated token
    public String createUser(NewUser newUser) {

        // to add email validation

        // check if user already exists
        boolean userExists = userRepository.
                findByEmail(newUser.getEmail())
                .isPresent();

        if (userExists) {
            throw  new IllegalStateException("email already taken");
        }

        // if not, create new user
        User user = new User(
            newUser.getFirstName(),
            newUser.getLastName(),
            newUser.getEmail(),
            bCryptPasswordEncoder.encode(
            newUser.getPassword()),
            setNewUserRole(newUser.getRole()),
            newUser.getPhoneNumber()
        );

        //save new user to db
        userRepository.save(user);

        // create confirmation token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
            token,
            LocalDateTime.now(),
            user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // create token confirmation link to be sent
        String link = "http://localhost:8080/api/admin/v1/registration/confirm?token=" + token;

        // then send token to email for confirmation
        emailSender.send(newUser.getEmail(),buildEmail(newUser.getFirstName(),link));
        return token;
    }

    // method to confirm the token
    @Transactional
    public String confirmToken(String token) {

        // get token, throw exception if token is not found.
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        // check if token has been confirmed before.
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }

        // confirm the token by setting the time token was confirmed.
        confirmationTokenService.setConfirmedAt(token);

        // after confirmation, enable the app user.
        userService.enableUser(confirmationToken.getUser().getEmail());

        return "confirmed";
    }

    // method to set role of new user
    public UserRole setNewUserRole(String role) {
        if (role.equals("TENANT")) {
            return UserRole.TENANT;
        } else if (role.equals("STAFF")) {
            return UserRole.STAFF;
        }
        return UserRole.ADMIN;
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n  <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}