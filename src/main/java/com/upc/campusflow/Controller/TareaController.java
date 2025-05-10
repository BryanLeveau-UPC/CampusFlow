package com.upc.campusflow.Controller;

import com.upc.campusflow.DTO.TareaDTO;
import com.upc.campusflow.Service.TareaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/tareas")
public class TareaController {
    final TareaService tareaService;

    public TareaController(TareaService tareaService) {this.tareaService = tareaService;}

    // Obtener lista de tareas
    @GetMapping
    public ResponseEntity<List<TareaDTO>> listar() { return ResponseEntity.ok(tareaService.listar()); }
    // Guardar
    @PostMapping
    public ResponseEntity<TareaDTO> guardar(@RequestBody TareaDTO tareaDTO) {
        return ResponseEntity.ok(tareaService.guardar(tareaDTO));  // Devuelve la tarea guardada con un código 201
    }

    // Modificar
    @PutMapping("/{id}")
    public ResponseEntity<TareaDTO> modificar(@PathVariable Long id, @RequestBody TareaDTO tareaDTO) {
        return ResponseEntity.ok(tareaService.modificar(id,tareaDTO));  // Si no existe, devuelve un 404
    }
    // Eliminar tarea por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<TareaDTO> eliminar(@PathVariable Long id) {
        return ResponseEntity.ok(tareaService.eliminar(id));  // Si no se encuentra, devuelve un 404
    }
    //Obtener todas las tareas activas de un estudiante específico
    @GetMapping("/estudiante/{idEstudiante}/activas")
    public ResponseEntity<List<TareaDTO>> TareasActivasPorEstudiante(@PathVariable Long idEstudiante) {
        List<TareaDTO> tareaDTOS = tareaService.TareasActivasPorEstudiante(idEstudiante);
        return ResponseEntity.ok(tareaDTOS);
    }
    //Obtener las tareas con una prioridad específica y ordenarlas por fecha límite
    @GetMapping("/prioridad/{prioridad}")
    public ResponseEntity<List<TareaDTO>> TareasPorPrioridad(@PathVariable String prioridad) {
        List<TareaDTO> tareaDTOS = tareaService.TareasPorPrioridad(prioridad);
        return ResponseEntity.ok(tareaDTOS);
    }
}
