package com.example.chatteryusermodule.services.auth;

import com.example.chatteryusermodule.config.jwt.JwtAuthenticationResponse;
import com.example.chatteryusermodule.config.jwt.JwtService;
import com.example.chatteryusermodule.dto.UserDto;
import com.example.chatteryusermodule.dto.auth.SignInUser;
import com.example.chatteryusermodule.dto.auth.SignUpUser;
import com.example.chatteryusermodule.models.User;
import com.example.chatteryusermodule.models.enums.StateVerification;
import com.example.chatteryusermodule.models.enums.UserRole;
import com.example.chatteryusermodule.services.base.UserService;
import com.example.chatteryusermodule.services.base.UserVerificationService;
import com.example.chatteryusermodule.services.email.EmailService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.example.chatteryusermodule.utils.CipherUtil.encryptId;

@Service
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserVerificationService userVerificationService;
    private final EmailService emailService;
    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(UserService userService, JwtService jwtService, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, UserVerificationService userVerificationService, EmailService emailService){
        this.userService = userService;
        this.jwtService = jwtService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.userVerificationService = userVerificationService;
        this.emailService = emailService;
    }

    public UserDto getUserFromToken(String jwtToken) {
        String username = getUsernameFromToken(jwtToken);
        return userService.getUserDtoFromOrigin(userService.getByEmail(username));
    }

    private String getUsernameFromToken(String jwtToken) {
        return jwtService.extractUserName(jwtToken);
    }

    public void getCodeRequest(String email) {
        User currentUser = userService.getByEmail(email);

        if (currentUser == null) {
            log.error("User password with email = {} not found", email);
            throw new EntityNotFoundException();
        }

        userVerificationService.createUserVerificationForUser(currentUser);

        emailService.sendSimpleEmail(currentUser.getEmail(), "Изменение пароля",
                "Введите данный код, чтобы изменить пароль: " + currentUser.getUserVerification().getHash() + ". Если Вы не делали запрос на изменение пароля, то просто проигнорируйте данное письмо.");
    }

    public Boolean checkEmailAndCode(String email, String code) {
        User currentUser = userService.getByEmail(email);
        LocalDateTime moment = LocalDateTime.now();

        if (currentUser == null) {
            log.error("User password with email = {} not found", email);
            return false;
        }

        if (currentUser.getUserVerification().getHash() == null) {
            log.error("UserVerification with id = {} not found", currentUser.getUserVerification().getId());
            return false;
        }

        if (currentUser.getUserVerification().getState() != StateVerification.ACTIVE ||
                currentUser.getUserVerification().getDeadline().isBefore(moment)) {
            userVerificationService.expiredUserVerificationForUser(currentUser.getUserVerification().getId());
            log.error("Confirmation was expired. Try again.");
            return false;
        }

        if (!Objects.equals(currentUser.getUserVerification().getHash(), code)) {
            log.error("Confirmation not equal. Try again.");
            return false;
        }

        return true;
    }

    public void confirmEmail(String email, String code, String newPassword, String newPasswordConfirmation) {
        User currentUser = userService.getByEmail(email);
        LocalDateTime moment = LocalDateTime.now();

        if (currentUser == null) {
            log.error("User password with email = {} not found", email);
            throw new EntityNotFoundException();
        }

        if (currentUser.getUserVerification().getHash() == null) {
            log.error("UserVerification with id = {} not found", currentUser.getUserVerification().getId());
            throw new NullPointerException();
        }

        if (currentUser.getUserVerification().getState() != StateVerification.ACTIVE ||
                currentUser.getUserVerification().getDeadline().isBefore(moment)) {
            userVerificationService.expiredUserVerificationForUser(currentUser.getUserVerification().getId());
            log.error("Confirmation was expired. Try again.");
            throw new IllegalArgumentException();
        }

        if (!Objects.equals(currentUser.getUserVerification().getHash(), code)) {
            log.error("Confirmation not equal. Try again.");
            throw new IllegalArgumentException();
        }

        if (!Objects.equals(newPassword, newPasswordConfirmation)) {
            log.error("Passwords not equals. Confirm password failed.");
            throw new IllegalArgumentException();
        }

        currentUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userVerificationService.expiredUserVerificationForUser(currentUser.getUserVerification().getId());

        userService.save(currentUser);
        log.info("User password with email = {} successfully edit", email);
    }

    @Transactional
    public JwtAuthenticationResponse signUp(SignUpUser request) {
        User user = User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .role(UserRole.SIMPLE)
                .build();

        if (Objects.equals(request.getPassword(), request.getPasswordConfirm())) {
            userService.add(userService.getUserCreateFromOrigin(user));
            String jwt = jwtService.generateToken(user);
            log.info("User with id = {} successfully sign up", user.getId());

            return new JwtAuthenticationResponse(jwt);
        }

        log.error("Passwords not equals");
        throw new IllegalArgumentException("Passwords not equals");
    }

    @Transactional
    public JwtAuthenticationResponse signUpAdmin(SignUpUser request) {
        User user = User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .role(UserRole.PRO)
                .build();

        if (Objects.equals(request.getPassword(), request.getPasswordConfirm())) {
            userService.add(userService.getUserCreateFromOrigin(user));
            String jwt = jwtService.generateToken(user);
            log.info("User with id = {} successfully sign up", user.getId());

            return new JwtAuthenticationResponse(jwt);
        }

        log.error("Passwords not equals");
        throw new IllegalArgumentException("Passwords not equals");
    }

    public JwtAuthenticationResponse signIn(SignInUser request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails user = (UserDetails) authentication.getPrincipal();

        String jwt = jwtService.generateToken(user);
        log.info("User with email = {} successfully signed in", request.getEmail());

        return new JwtAuthenticationResponse(jwt);
    }

    public UserDto getCurrentUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            log.error("Authentication failed.");
            throw new AuthenticationCredentialsNotFoundException("Authentication failed.");
        }

        User user = userService.getByEmail(userDetails.getUsername());
        if (user == null) {
            log.error("User not found.");
            throw new NullPointerException();
        }

        return UserDto.builder()
                .id(encryptId(user.getId()))
                .nickname(user.getNickname())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
