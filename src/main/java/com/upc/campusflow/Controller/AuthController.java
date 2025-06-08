package com.upc.campusflow.Controller;

import com.upc.campusflow.Model.Rol;
import com.upc.campusflow.Model.Usuario;
import com.upc.campusflow.Repository.RolRepository;
import com.upc.campusflow.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    // DTO para el registro, puedes adaptarlo a tu caso
    public static class RegisterRequest {
        private String username;
        private String password;
        private Long rolId;

        // Getters y setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public Long getRolId() { return rolId; }
        public void setRolId(Long rolId) { this.rolId = rolId; }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Crear un nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(request.getUsername());
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));
        nuevoUsuario.setEstado(true);

        // Asignar el rol al nuevo usuario
        Rol rolUsuario = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + request.getRolId()));
        nuevoUsuario.setRoles(Collections.singletonList(rolUsuario));

        // Guardar el nuevo usuario
        usuarioRepository.save(nuevoUsuario);

        // Devolver la respuesta
        return ResponseEntity.ok(Map.of("body", "Usuario registrado correctamente"));
    }
}
