package com.upc.campusflow.Controller;


import com.upc.campusflow.DTO.PublicacionDTO;
import com.upc.campusflow.Service.PublicacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/Publicacion")
public class PublicacionController {

    final PublicacionService publicacionService;

    Logger log = Logger.getLogger(PublicacionController.class.getName());

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @GetMapping
    public ResponseEntity<List<PublicacionDTO>> listar() {
        return ResponseEntity.ok(publicacionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionDTO> buscarPorId(@PathVariable Long id) {
        PublicacionDTO dto = publicacionService.buscarPorId(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PublicacionDTO> guardar(@RequestBody PublicacionDTO publicacionDTO) {
        log.info("Guardando publicación: " + publicacionDTO.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(publicacionService.guardar(publicacionDTO));
    }
    @PutMapping("/{id}")
    public ResponseEntity<PublicacionDTO> modificar(@PathVariable Long id, @RequestBody PublicacionDTO publicacionDTO) {
        PublicacionDTO actualizado = publicacionService.modificar(id, publicacionDTO);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PublicacionDTO> eliminar(@PathVariable Long id) {
        PublicacionDTO eliminado = publicacionService.eliminar(id);
        return ResponseEntity.ok(eliminado);
    }

    @GetMapping("/resumen/label")
    public ResponseEntity<Map<String, Long>> resumenPorLabel() {
        Map<String, Long> resumen = publicacionService.resumenPorLabel();
        return ResponseEntity.ok(resumen);
    }
}
