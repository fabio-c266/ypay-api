package com.ypay.api.repositories;

import com.ypay.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByCpf(String cpf);
    boolean existsByCnpj(String cnpj);

    Optional<User> findByEmail(String email);
}
