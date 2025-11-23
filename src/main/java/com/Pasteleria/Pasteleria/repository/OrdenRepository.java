package com.Pasteleria.Pasteleria.repository;

import com.Pasteleria.Pasteleria.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
}
