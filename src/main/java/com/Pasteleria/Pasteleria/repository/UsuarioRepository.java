package com.Pasteleria.Pasteleria.repository;

import com.Pasteleria.Pasteleria.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método para buscar un usuario por su email
    Optional<Usuario> findByEmail(String email);

    // Método para saber si un email ya existe
    Boolean existsByEmail(String email);
}