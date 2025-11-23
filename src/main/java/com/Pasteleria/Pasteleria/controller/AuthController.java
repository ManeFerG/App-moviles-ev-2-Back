package com.Pasteleria.Pasteleria.controller;

import com.Pasteleria.Pasteleria.dto.JwtAuthResponseDto;
import com.Pasteleria.Pasteleria.dto.LoginDto;
import com.Pasteleria.Pasteleria.dto.RegistroDto;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Registro y login de usuarios")
@CrossOrigin(origins = {"*"}) 
public class AuthController {

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        try {
            
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );

            
            SecurityContextHolder.getContext().setAuthentication(authentication);

            
            Usuario usuario = usuarioRepository.findByEmail(loginDto.getEmail())
                    .orElseThrow(() ->
                            new UsernameNotFoundException("Usuario no encontrado con email: " + loginDto.getEmail())
                    );

            
            String token = jwtTokenProvider.generarToken(usuario.getEmail());

            
            JwtAuthResponseDto response = new JwtAuthResponseDto();
            response.setAccessToken(token);
            response.setNombre(usuario.getNombre());
            response.setEmail(usuario.getEmail());

            String rol = usuario.getRoles().stream()
                    .map(Rol::getNombre)
                    .findFirst()
                    .orElse(null);
            response.setRol(rol);

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas: " + e.getMessage());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Error de autenticación: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al hacer login: " + e.getMessage());
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody RegistroDto registroDto) {

        if (usuarioRepository.existsByEmail(registroDto.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("El email ya está registrado");
        }

        Usuario usuario = new Usuario(
                registroDto.getNombre(),
                registroDto.getEmail(),
                passwordEncoder.encode(registroDto.getPassword())
        );

        Rol rolCliente = rolRepository.findByNombre("ROLE_CLIENTE")
                .orElseThrow(() ->
                        new RuntimeException("Error: Rol 'ROLE_CLIENTE' no encontrado.")
                );

        usuario.setRoles(Collections.singleton(rolCliente));
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
}
