package com.upc.campusflow.Controller;

import com.upc.campusflow.DTO.ProfesorDTO;
import com.upc.campusflow.Service.ProfesorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesores")
public class ProfesorController {
    final ProfesorService profesorService;

    public ProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    //listar
    @GetMapping
    public ResponseEntity<List<ProfesorDTO>> listar(){
        return ResponseEntity.ok(profesorService.listar());
    }

    //guardar
    @PostMapping
    public ResponseEntity<ProfesorDTO> guardar(@RequestBody ProfesorDTO profesorDTO){
        return ResponseEntity.ok(profesorService.guardar(profesorDTO));
    }
    // Modificar
    @PutMapping("/{id}")
    public ResponseEntity<ProfesorDTO> modificar(@PathVariable Long id, @RequestBody ProfesorDTO profesorDTO) {
        return ResponseEntity.ok(profesorService.modificar(id, profesorDTO));
    }

    // Eliminar lógico
    @DeleteMapping("/{id}")
    public ResponseEntity<ProfesorDTO> eliminarLogico(@PathVariable Long id) {
        return ResponseEntity.ok(profesorService.eliminar(id));
    }


}
