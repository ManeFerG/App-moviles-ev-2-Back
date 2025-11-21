package com.Pasteleria.Pasteleria.repository;

import com.Pasteleria.Pasteleria.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    // Método para buscar un rol por su nombre
    Optional<Rol> findByNombre(String nombre);
}