package com.upc.campusflow.Controller;

import com.upc.campusflow.DTO.TareaDTO;
import com.upc.campusflow.Service.TareaService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/tareas")
public class TareaController {
    private final TareaService tareaService;
    private final MessageSource messageSource;

    // Logger para la bitácora
    Logger log = Logger.getLogger(TareaController.class.getName());

    public TareaController(TareaService tareaService, MessageSource messageSource) {
        this.tareaService = tareaService;
        this.messageSource = messageSource;
    }
    // Obtener lista de tareas
    @GetMapping
    public ResponseEntity<List<TareaDTO>> listar() {
        List<TareaDTO> tareas = tareaService.listar();  // Llama al servicio para obtener las tareas
        return ResponseEntity.ok(tareas);  // Devuelve la lista de tareas en la respuesta HTTP
    }
    // Obtener tarea por ID
    @GetMapping("/{id}")
    public ResponseEntity<TareaDTO> buscarPorId(@PathVariable int id) {
        TareaDTO tareaDTO = tareaService.buscarPorId(id);  // Llama al servicio para obtener la tarea por ID
        if (tareaDTO != null) {
            return ResponseEntity.ok(tareaDTO);  // Si la tarea existe, la devuelve
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Si no, devuelve un 404
    }

    // Crear una nueva tarea
    @PostMapping
    public ResponseEntity<TareaDTO> guardar(@RequestBody TareaDTO tareaDTO) {
        log.info("Guardando tarea: " + tareaDTO.toString());  // Log para saber que se guarda la tarea
        TareaDTO tareaGuardada = tareaService.guardar(tareaDTO);  // Llama al servicio para guardar la tarea
        return ResponseEntity.status(HttpStatus.CREATED).body(tareaGuardada);  // Devuelve la tarea guardada con un código 201
    }

    // Actualizar tarea existente
    @PutMapping("/{id}")
    public ResponseEntity<TareaDTO> actualizar(@PathVariable int id, @RequestBody TareaDTO tareaDTO) {
        TareaDTO tareaActualizada = tareaService.actualizar(id, tareaDTO);  // Llama al servicio para actualizar la tarea
        if (tareaActualizada != null) {
            return ResponseEntity.ok(tareaActualizada);  // Si se actualizó correctamente, devuelve la tarea actualizada
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Si no existe, devuelve un 404
    }
    // Eliminar tarea por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        boolean eliminado = tareaService.eliminar(id);  // Llama al servicio para eliminar la tarea
        if (eliminado) {
            return ResponseEntity.noContent().build();  // Si se eliminó, devuelve un 204 No Content
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Si no se encuentra, devuelve un 404
    }
}
