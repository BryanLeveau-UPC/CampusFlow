package com.upc.campusflow.Controller;

import com.upc.campusflow.DTO.EstudianteDTO;
import com.upc.campusflow.Service.EstudianteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
@CrossOrigin
@RestController
@RequestMapping("/Estudiante")
public class EstudianteController {

    final EstudianteService estudianteService;

    Logger log = Logger.getLogger(EstudianteController.class.getName());

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping
    public ResponseEntity<List<EstudianteDTO>> listar() {
        return ResponseEntity.ok(estudianteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteDTO> buscarPorId(@PathVariable Long id) {
        EstudianteDTO estudianteDTO = estudianteService.buscarPorId(id);
        if (estudianteDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estudianteDTO);
    }
    @GetMapping("/promedio/menor-a-11")
    public ResponseEntity<List<EstudianteDTO>> obtenerEstudiantesConNotaBaja() {
        return ResponseEntity.ok(estudianteService.obtenerEstudiantesConNotaBaja());
    }

    @PostMapping
    public ResponseEntity<EstudianteDTO> guardar(@RequestBody EstudianteDTO estudianteDTO) {
        log.info("Guardando estudiante: " + estudianteDTO.toString());
        EstudianteDTO guardado = estudianteService.guardar(estudianteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteDTO> modificar(@PathVariable Long id, @RequestBody EstudianteDTO estudianteDTO) {
        EstudianteDTO actualizado = estudianteService.modificar(id, estudianteDTO);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EstudianteDTO> eliminar(@PathVariable Long id) {
        EstudianteDTO eliminado = estudianteService.eliminar(id);
        return ResponseEntity.ok(eliminado);
    }


    @GetMapping("/top-decimo")
    public ResponseEntity<List<EstudianteDTO>> obtenerTopDecile() {
        List<EstudianteDTO> lista = estudianteService.topDecilePorNota();
        return ResponseEntity.ok(lista);
    }

}
