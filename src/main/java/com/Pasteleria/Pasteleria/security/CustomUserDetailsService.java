package com.Pasteleria.Pasteleria.security;

import com.Pasteleria.Pasteleria.model.Usuario;
import com.Pasteleria.Pasteleria.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Inyectamos el repositorio que creamos en el Paso 3
    private UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Le decimos a Spring que busque al usuario por email
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Convertimos los roles de nuestra BD a los "Roles" que Spring entiende
        Collection<? extends GrantedAuthority> authorities = usuario.getRoles().stream()
            .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
            .collect(Collectors.toList());

        // Devolvemos el usuario de Spring
        return new User(usuario.getEmail(), usuario.getPassword(), authorities);
    }
}