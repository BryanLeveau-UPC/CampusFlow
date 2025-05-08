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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

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
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }

        Rol rolUsuario = rolRepository.findByNombre("ROLE_USUARIO")
                .orElseGet(() -> rolRepository.save(new Rol(null, "ROLE_USUARIO", null)));

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(request.getUsername());
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setNombre(request.getNombre());
        nuevoUsuario.setApellido(request.getApellido());
        nuevoUsuario.setEstado(true);
        nuevoUsuario.setRoles(Collections.singletonList(rolUsuario)); // se asigna automáticamente

        usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.ok("Usuario registrado correctamente");
    }
}
