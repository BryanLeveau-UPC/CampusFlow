package com.upc.campusflow.Controller;

import com.upc.campusflow.DTO.AsignaturaDTO;
import com.upc.campusflow.DTO.NotaDTO;
import com.upc.campusflow.Service.AsignaturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asignatura")
public class AsignaturaController {
    final AsignaturaService asignaturaService;

    public AsignaturaController(AsignaturaService asignaturaService) {
        this.asignaturaService = asignaturaService;
    }
    //listar
    @GetMapping
    public ResponseEntity<List<AsignaturaDTO>> Listar(){
        return ResponseEntity.ok(asignaturaService.Listar());
    }
    //guardar
    @PostMapping
    public ResponseEntity<AsignaturaDTO> guardar(@RequestBody AsignaturaDTO asignaturaDTO){
        return ResponseEntity.ok(asignaturaService.guardar(asignaturaDTO));
    }
    // Modificar
    @PutMapping("/{id}")
    public ResponseEntity<AsignaturaDTO> modificar(@PathVariable Long id, @RequestBody AsignaturaDTO asignaturaDTO) {
        return ResponseEntity.ok(asignaturaService.modificar(id, asignaturaDTO));
    }

    // Eliminar lógico
    @DeleteMapping("/{id}")
    public ResponseEntity<AsignaturaDTO> eliminarLogico(@PathVariable Long id) {
        return ResponseEntity.ok(asignaturaService.eliminar(id));
    }

    // Endpoint para obtener las 3 asignaturas con el mayor promedio
    @GetMapping("/nota/top-3-promedio")
    public List<AsignaturaDTO> obtenerTop3AsignaturasConMayorPromedio() {
        return asignaturaService.obtenerTop3AsignaturasConMayorPromedio();
    }

}
