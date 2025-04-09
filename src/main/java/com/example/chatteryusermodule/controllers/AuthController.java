package com.example.chatteryusermodule.controllers;

import com.example.chatteryusermodule.config.jwt.JwtAuthenticationResponse;
import com.example.chatteryusermodule.dto.UserDto;
import com.example.chatteryusermodule.dto.auth.SignInUser;
import com.example.chatteryusermodule.dto.auth.SignUpUser;
import com.example.chatteryusermodule.services.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/getByToken")
    public ResponseEntity<UserDto> getByToken(@RequestParam String jwtToken) {
        return new ResponseEntity<>(authenticationService.getUserFromToken(jwtToken), HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpUser request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/signUpAdmin")
    public JwtAuthenticationResponse signUpAdmin(@RequestBody @Valid SignUpUser request) {
        return authenticationService.signUpAdmin(request);
    }

    @PostMapping("/signIn")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInUser request) {
        return authenticationService.signIn(request);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDto user = authenticationService.getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> check(@RequestParam String email,
                                         @RequestParam String code) {
        return new ResponseEntity<>(authenticationService.checkEmailAndCode(email, code), HttpStatus.OK);
    }

    @GetMapping("/sendCode")
    public void sendCodeOnEmail(@RequestParam String email) {
        authenticationService.getCodeRequest(email);
    }

    @PatchMapping("/editPassword")
    public void editPassword(@RequestParam String email,
                             @RequestParam String code,
                             @RequestParam String newPassword,
                             @RequestParam String confirmPassword) {
        authenticationService.confirmEmail(email, code, newPassword, confirmPassword);
    }
}
