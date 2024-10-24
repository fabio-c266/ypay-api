package com.ypay.api.services;

import com.ypay.api.domain.user.User;
import com.ypay.api.dtos.LoginUserDTO;
import com.ypay.api.infra.exceptions.AuthException;
import com.ypay.api.infra.exceptions.EntityNotFoundException;
import com.ypay.api.infra.exceptions.InvalidCredentialsException;
import com.ypay.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public String login(LoginUserDTO loginUserDTO) throws InvalidCredentialsException, AuthException {
        User user = null;

        try {
            user = this.userService.getByEmail(loginUserDTO.email());
        } catch (EntityNotFoundException ignored) {
            throw new InvalidCredentialsException();
        }

        if (!passwordEncoder.matches(loginUserDTO.password(), user.getPasswordHashed())) {
            throw new InvalidCredentialsException();
        }

        String token = this.tokenService.generateToken(user);

        if (token == null) {
            throw new AuthException();
        }

        return token;
    }
}
