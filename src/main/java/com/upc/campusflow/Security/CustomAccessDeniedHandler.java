package com.upc.campusflow.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // Para formatear la fecha
import java.util.HashMap;
import java.util.Map;

@Component // Marca esta clase como un componente de Spring
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // Puedes loguear la excepción para depuración (opcional)
        System.err.println("Acceso Denegado por Spring Security: " + accessDeniedException.getMessage());

        // Configura la respuesta HTTP
        response.setStatus(HttpStatus.FORBIDDEN.value()); // Código 403 Forbidden
        response.setContentType("application/json"); // Tipo de contenido JSON
        response.setCharacterEncoding("UTF-8"); // Codificación de caracteres

        // Crea el cuerpo de la respuesta JSON
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("mensaje", "No hay permisos suficientes para esta operación.");
        errorDetails.put("timestamp", LocalDateTime.now().format(FORMATTER));
        errorDetails.put("status", HttpStatus.FORBIDDEN.value());

        // Convierte el mapa a JSON y escribe en la respuesta
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), errorDetails);
    }
}