package com.upc.campusflow.Controller;

import com.upc.campusflow.DTO.EstudianteDTO;
import com.upc.campusflow.Service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/estudiante")
public class EstudianteController {

    private final EstudianteService estudianteService;

    @Autowired
    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @PostMapping
    public ResponseEntity<EstudianteDTO> crear(@RequestBody EstudianteDTO dto) {
        EstudianteDTO creado = estudianteService.guardar(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EstudianteDTO>> listarTodos() {
        List<EstudianteDTO> lista = estudianteService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteDTO> obtenerPorId(@PathVariable Long id) {
        EstudianteDTO dto = estudianteService.obtenerPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteDTO> actualizar(@PathVariable Long id, @RequestBody EstudianteDTO dto) {
        EstudianteDTO actualizado = estudianteService.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        estudianteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints para consultas personalizadas
    @GetMapping("/notas/menor11")
    public ResponseEntity<List<EstudianteDTO>> estudiantesConNotaMenorA11() {
        List<EstudianteDTO> lista = estudianteService.findEstudiantesConNotaMenorA11();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/top")
    public ResponseEntity<List<EstudianteDTO>> topPromedio(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<EstudianteDTO> lista = estudianteService.findAllOrderByAverageNotaDesc(PageRequest.of(page, size));
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/participantes")
    public ResponseEntity<List<EstudianteDTO>> participantesPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<EstudianteDTO> lista = estudianteService.obtenerPorRangoDeFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(lista);
    }
}
