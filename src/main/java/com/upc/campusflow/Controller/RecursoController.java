package com.upc.campusflow.Controller;

import com.upc.campusflow.DTO.RecursoDTO;
import com.upc.campusflow.Service.RecursoService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/recursos")
public class RecursoController {
    private final RecursoService recursoService;
    private final MessageSource messageSource;

    // Logger para la bitácora
    Logger log = Logger.getLogger(RecursoController.class.getName());

    public RecursoController(RecursoService recursoService, MessageSource messageSource) {
        this.recursoService = recursoService;
        this.messageSource = messageSource;
    }

    // Obtener lista de recursos
    @GetMapping
    public ResponseEntity<List<RecursoDTO>> listar() {
        List<RecursoDTO> recursos = recursoService.listar();  // Llama al servicio para obtener los recursos
        return ResponseEntity.ok(recursos);  // Devuelve la lista de recursos en la respuesta HTTP
    }

    // Obtener recurso por ID
    @GetMapping("/{id}")
    public ResponseEntity<RecursoDTO> buscarPorId(@PathVariable int id) {
        RecursoDTO recursoDTO = recursoService.buscarPorId(id);  // Llama al servicio para obtener el recurso por ID
        if (recursoDTO != null) {
            return ResponseEntity.ok(recursoDTO);  // Si el recurso existe, lo devuelve
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Si no, devuelve un 404
    }

    // Crear un nuevo recurso
    @PostMapping
    public ResponseEntity<RecursoDTO> guardar(@RequestBody RecursoDTO recursoDTO) {
        log.info("Guardando recurso: " + recursoDTO.toString());  // Log para saber que se guarda el recurso
        RecursoDTO recursoGuardado = recursoService.guardar(recursoDTO);  // Llama al servicio para guardar el recurso
        return ResponseEntity.status(HttpStatus.CREATED).body(recursoGuardado);  // Devuelve el recurso guardado con un código 201
    }
}
