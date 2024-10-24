package com.ypay.api.controllers;

import com.ypay.api.domain.user.User;
import com.ypay.api.dtos.CreateUserDTO;
import com.ypay.api.dtos.UpdateUserDTO;
import com.ypay.api.dtos.UserResponseDTO;
import com.ypay.api.infra.exceptions.ConflictException;
import com.ypay.api.infra.exceptions.DTOEmptyException;
import com.ypay.api.infra.exceptions.EntityNotFoundException;
import com.ypay.api.infra.exceptions.ValidationException;
import com.ypay.api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid CreateUserDTO createUserDTO) throws ValidationException, ConflictException, EntityNotFoundException {
        User userCreated = this.userService.create(createUserDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDTO(userCreated));
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> get() throws EntityNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = this.userService.getByEmail(email);
        return ResponseEntity.ok(
                new UserResponseDTO(user)
        );
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> update(@RequestBody @Valid UpdateUserDTO updateUserDTO) throws DTOEmptyException, ConflictException, EntityNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = this.userService.update(email, updateUserDTO);

        return ResponseEntity.ok(
                new UserResponseDTO(user)
        );
    }

    @GetMapping("/toggle-active")
    public ResponseEntity<Void> toggleIsActive() throws EntityNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        this.userService.toggleIsActive(email);

        return ResponseEntity.ok().build();
    }
}
