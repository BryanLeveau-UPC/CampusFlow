package com.upc.campusflow.Controller;

import com.upc.campusflow.Model.Rol;
import com.upc.campusflow.Model.Usuario;
import com.upc.campusflow.Repository.RolRepository;
import com.upc.campusflow.Repository.UsuarioRepository;
import com.upc.campusflow.Security.CustomUserDetailsService;
import com.upc.campusflow.Security.JwtTokenUtil;
import com.upc.campusflow.DTO.AuthRequest;
import com.upc.campusflow.DTO.AuthResponse;
import com.upc.campusflow.DTO.RegisterRequest;
import com.upc.campusflow.Service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.Map;
@CrossOrigin

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolService rolService;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            String token = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Verificar si el nombre de usuario ya existe
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }

        // Buscar el rol por ID
        Rol rolUsuario = rolRepository.findById(request.getRolId()).orElse(null);

        // Si el rol no existe, devolver error
        if (rolUsuario == null) {
            return ResponseEntity.badRequest().body("Rol no encontrado");
        }

        // Crear un nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(request.getUsername());
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setNombre(request.getNombre());
        nuevoUsuario.setApellido(request.getApellido());
        nuevoUsuario.setEstado(true);

        // Asignar el rol al nuevo usuario
        nuevoUsuario.setRol(rolUsuario);  // Asignamos el rol basado en su ID

        // Guardar el nuevo usuario
        usuarioRepository.save(nuevoUsuario);

        // Devolver la respuesta
        return ResponseEntity.ok("Usuario registrado correctamente");
    }
}
