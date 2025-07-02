package com.upc.campusflow.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration; // Importar
import org.springframework.web.cors.CorsConfigurationSource; // Importar
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Importar

import java.util.Arrays; // Importar

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // Configuración CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // <-- ¡Esta línea es clave!
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (no requieren autenticación)
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/carrera").permitAll()
                        .requestMatchers("/estudiante/register").permitAll()
                        .requestMatchers("/profesor/register").permitAll()
                        // Swagger
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/webjars/**",
                                "/swagger-resources/**"
                        ).permitAll()

                        // --- REGLAS ACTUALIZADAS PARA /usuarios ---
                        // Permitir a ESTUDIANTE, PROFESOR y ADMIN acceder a GET /usuarios/{id} (para obtener sus propios datos)
                        // Asegúrate de que los roles coincidan exactamente con los que tu JWT contiene (ej. "ESTUDIANTE" o "ROLE_ESTUDIANTE")
                        .requestMatchers(HttpMethod.GET, "/usuarios/{id}").hasAnyRole("ESTUDIANTE", "PROFESOR", "ADMIN")
                        // Solo ADMIN puede listar todos los usuarios (GET /usuarios)
                        .requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMIN")
                        // Solo ADMIN puede crear, modificar o eliminar usuarios directamente a través de /usuarios
                        .requestMatchers(HttpMethod.POST, "/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/usuarios/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/{id}").hasRole("ADMIN")

                        // Otras rutas que requieren rol ADMIN (mantener como estaban)
                        .requestMatchers("/profesor").hasRole("ADMIN")
                        .requestMatchers("//tareas").hasRole("ADMIN")
                        .requestMatchers("/recursos").hasRole("ADMIN")
                        .requestMatchers("/publicacion").hasRole("ADMIN")
                        .requestMatchers("/nota").hasRole("ADMIN")
                        .requestMatchers("/horarios").hasRole("ADMIN")
                        .requestMatchers("/grupoForo").hasRole("ADMIN")
                        .requestMatchers("/evento").hasRole("ADMIN")
                        .requestMatchers("/estudiante-estadística").hasRole("ADMIN")
                        .requestMatchers("/estudiante").hasRole("ADMIN")
                        .requestMatchers("/carrera").hasRole("ADMIN")
                        .requestMatchers("/asignatura").hasRole("ADMIN")

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                                .accessDeniedHandler(customAccessDeniedHandler)
                        // .authenticationEntryPoint(customAuthenticationEntryPoint) // <--- Add this if you also created CustomAuthenticationEntryPoint for 401s
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para la configuración CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite peticiones desde tu origen Angular
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // <--- ¡Importante!
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // <--- ¡Importante para JWT!
        configuration.setAllowCredentials(true); // Permite el envío de cookies/credenciales (aunque JWT no las usa directamente, es buena práctica)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica esta configuración a todas las rutas
        return source;
    }
}
    