package com.upc.campusflow.Controller;

import com.upc.campusflow.DTO.EstudianteEstadisticaDTO;
import com.upc.campusflow.DTO.NotaDTO;
import com.upc.campusflow.Service.NotaService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nota")
public class NotaController {
    final NotaService notaService;

    public NotaController(NotaService notaService) {
        this.notaService = notaService;
    }
    //listar
    @GetMapping
    public ResponseEntity<List<NotaDTO>> Listar(){
        return ResponseEntity.ok(notaService.Listar());
    }
    //guardar
    @PostMapping
    public ResponseEntity<NotaDTO> guardar(@RequestBody NotaDTO notaDTO){
        return ResponseEntity.ok(notaService.guardar(notaDTO));
    }
    // Modificar
    @PutMapping("/{id}")
    public ResponseEntity<NotaDTO> modificar(@PathVariable Long id, @RequestBody NotaDTO notaDTO) {
        return ResponseEntity.ok(notaService.modificar(id, notaDTO));
    }

    // Eliminar lógico
    @DeleteMapping("/{id}")
    public ResponseEntity<NotaDTO> eliminarLogico(@PathVariable Long id) {
        return ResponseEntity.ok(notaService.eliminar(id));
    }


    @GetMapping("/asignatura/{id}")
    public ResponseEntity<List<NotaDTO>> obtenerNotasPorAsignatura(@PathVariable("id") Long idAsignatura) {
        return ResponseEntity.ok(notaService.obtenerNotasPorAsignatura(idAsignatura));
    }
}
