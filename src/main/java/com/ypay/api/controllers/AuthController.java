package com.ypay.api.controllers;

import com.ypay.api.dtos.LoginUserDTO;
import com.ypay.api.dtos.TokenResponseDTO;
import com.ypay.api.infra.exceptions.AuthException;
import com.ypay.api.infra.exceptions.InvalidCredentialsException;
import com.ypay.api.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginUserDTO loginUserDTO) throws InvalidCredentialsException, AuthException {
        String token = this.authService.login(loginUserDTO);

        return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}
