package com.ypay.api.domain.user;

import com.ypay.api.domain.address.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "user_type")
    private UserType userType;

    private BigDecimal balance;
    private String email;
    private String phone;
    private String cpf;
    private String cnpj;

    @Column(name = "active")
    private boolean isActive;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
