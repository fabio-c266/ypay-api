package com.ypay.api.repositories;

import com.ypay.api.domain.address.UF;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UFRepository extends JpaRepository<UF, Integer> {
}
