package com.upc.campusflow.Controller;
import com.upc.campusflow.DTO.ErrorRespuesta;
import com.upc.campusflow.Exception.AccesoDenegadoException;
import com.upc.campusflow.Exception.RecursoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // Para formatear la fecha
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // --- Manejo de RecursoNoEncontradoException (404 Not Found) ---
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        ErrorRespuesta error = new ErrorRespuesta(
                ex.getMessage(),
                LocalDateTime.now().format(FORMATTER), // Formateamos la fecha
                HttpStatus.NOT_FOUND.value() // Obtenemos el valor entero del estado HTTP (404)
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // --- Manejo de ModelMapper.ConfigurationException (adaptación para tu error anterior) ---
    // Este error ocurre cuando ModelMapper no puede resolver ambigüedades en el mapeo
    @ExceptionHandler(org.modelmapper.ConfigurationException.class)
    public ResponseEntity<ErrorRespuesta> manejarModelMapperConfigurationException(org.modelmapper.ConfigurationException ex) {
        ErrorRespuesta error = new ErrorRespuesta(
                "Error de configuración en el mapeo de datos: " + ex.getMessage(),
                LocalDateTime.now().format(FORMATTER),
                HttpStatus.INTERNAL_SERVER_ERROR.value() // Normalmente un 500 para errores de configuración
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // --- Manejo de HibernateException: "identifier of an instance was altered" (adaptación para tu error anterior) ---
    // Este error ocurre cuando intentas cambiar el ID de una entidad ya gestionada por Hibernate.
    @ExceptionHandler(org.hibernate.HibernateException.class)
    public ResponseEntity<ErrorRespuesta> manejarHibernateException(org.hibernate.HibernateException ex) {
        ErrorRespuesta error = new ErrorRespuesta(
                "Error al intentar modificar un identificador de entidad: " + ex.getMessage(),
                LocalDateTime.now().format(FORMATTER),
                HttpStatus.BAD_REQUEST.value() // Podría ser 400 Bad Request si el cliente intentó cambiar el ID
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    // --- Manejo de validaciones de DTOs (@Valid, 400 Bad Request) ---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarArgumentosInvalidos(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField(); // Casteo a FieldError para obtener el campo
            String errorMessage = error.getDefaultMessage();
            errores.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }

    // --- Manejo de cualquier otra excepción no esperada (500 Internal Server Error) ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> manejarOtrasExcepciones(Exception ex) {
        // MUY IMPORTANTE: Loguea la excepción completa aquí para depuración en tu servidor
        ex.printStackTrace(); // O usa un logger como SLF4J/Logback: log.error("Error no manejado: ", ex);

        ErrorRespuesta error = new ErrorRespuesta(
                "Ocurrió un error inesperado en el sistema. Por favor, contacta a soporte.",
                LocalDateTime.now().format(FORMATTER),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // --- NUEVO: Manejo de AccesoDenegadoException (403 Forbidden) ---
    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<ErrorRespuesta> manejarAccesoDenegado(AccesoDenegadoException ex) {
        ErrorRespuesta error = new ErrorRespuesta(
                ex.getMessage(),
                LocalDateTime.now().format(FORMATTER),
                HttpStatus.FORBIDDEN.value() // El código HTTP 403 Forbidden
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}