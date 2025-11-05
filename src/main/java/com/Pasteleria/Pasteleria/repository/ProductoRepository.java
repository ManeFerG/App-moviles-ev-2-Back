package com.Pasteleria.Pasteleria.repository;

import org.springframework.data.jpa.repository.JpaRepository;// <-- 1. Import NUEVO
import org.springframework.stereotype.Repository;
import com.Pasteleria.Pasteleria.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
}