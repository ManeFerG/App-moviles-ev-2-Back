package com.Pasteleria.Pasteleria.controller;

// Importa las clases DTO que ya creaste
import com.Pasteleria.Pasteleria.dto.LoginDto;
import com.Pasteleria.Pasteleria.dto.RegistroDto;
import com.Pasteleria.Pasteleria.dto.JwtAuthResponseDto;

// Importa todas las demás clases que hemos creado
import com.Pasteleria.Pasteleria.model.Rol;
import com.Pasteleria.Pasteleria.model.Usuario;
import com.Pasteleria.Pasteleria.repository.RolRepository;
import com.Pasteleria.Pasteleria.repository.UsuarioRepository;
import com.Pasteleria.Pasteleria.security.JwtTokenProvider;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth") // Todas las rutas de este controller empezarán con /auth
@Tag(name = "Autenticación", description = "Operaciones de Registro y Login")
@CrossOrigin(origins = {"*"}) // Permitimos peticiones del frontend
public class AuthController {

    // Inyectamos todas las herramientas que necesitamos
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Endpoint para INICIAR SESIÓN
     */
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> authenticateUser(@RequestBody LoginDto loginDto){
        
        // 1. Usamos el AuthenticationManager (de SecurityConfig) para validar al usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        // 2. Establecemos la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Usamos nuestro Generador de Tokens (JwtTokenProvider) para crear el token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String token = jwtTokenProvider.generarToken(userDetails.getUsername());


        // 4. Devolvemos el token al frontend en la "cajita" DTO
        return ResponseEntity.ok(new JwtAuthResponseDto(token));
    }

    /**
     * Endpoint para REGISTRAR UN NUEVO USUARIO
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registerUser(@RequestBody RegistroDto registroDto){

        // 1. Verificamos si el email ya existe en la BD
        if(usuarioRepository.existsByEmail(registroDto.getEmail())){
            return new ResponseEntity<>("El email ya está en uso.", HttpStatus.BAD_REQUEST);
        }

        // 2. Creamos el nuevo usuario
        Usuario usuario = new Usuario(
            registroDto.getNombre(),
            registroDto.getEmail(),
            passwordEncoder.encode(registroDto.getPassword()) // ¡Encriptamos la contraseña!
        );

        // 3. Asignamos un rol por defecto "ROLE_USER"
        // (Para tu "ventana de admin", deberías crear un usuario con "ROLE_ADMIN" manualmente en tu BD)
        Rol roles = rolRepository.findByNombre("ROLE_USER").orElseThrow(() -> new RuntimeException("Error: Rol 'ROLE_USER' no encontrado."));
        usuario.setRoles(Collections.singleton(roles));

        // 4. Guardamos el usuario en la BD
        usuarioRepository.save(usuario);

        return new ResponseEntity<>("Usuario registrado exitosamente", HttpStatus.OK);
    }
}