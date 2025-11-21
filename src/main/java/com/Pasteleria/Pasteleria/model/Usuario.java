package com.Pasteleria.Pasteleria.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios", 
       uniqueConstraints = { 
           @UniqueConstraint(columnNames = "email") // mail unico
       })
@Data
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // Aquí guardaremos la contraseña HASHEADA

    // Relación Muchos-a-Muchos con Rol
    // FetchType.EAGER carga los roles junto con el usuario
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "usuario_roles", 
               joinColumns = @JoinColumn(name = "usuario_id"), 
               inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();

    public Usuario(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }
}