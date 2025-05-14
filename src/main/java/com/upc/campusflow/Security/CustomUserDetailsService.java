package com.upc.campusflow.Security;

import com.upc.campusflow.Model.Usuario;
import com.upc.campusflow.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.isEstado(),
                true,
                true,
                true,
                Collections.singletonList(
                        new SimpleGrantedAuthority(usuario.getRol().getNombre()) // <- Aquí se añade el rol, como "ADMIN"
                )
        );
    }
}
