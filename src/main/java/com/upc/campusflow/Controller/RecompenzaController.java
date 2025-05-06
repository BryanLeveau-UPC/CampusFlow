package com.upc.campusflow.Controller;

import com.upc.campusflow.DTO.EstudianteEstadisticaDTO;
import com.upc.campusflow.DTO.RecompenzaDTO;
import com.upc.campusflow.Model.Recompenza;
import com.upc.campusflow.Service.RecompenzaService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reocmpenza")
public class RecompenzaController {
    final RecompenzaService recompenzaService;

    public RecompenzaController(RecompenzaService recompenzaService) {
        this.recompenzaService = recompenzaService;
    }
    //listar
    @GetMapping
    public ResponseEntity<List<RecompenzaDTO>> Listar(){
        return ResponseEntity.ok(recompenzaService.Listar());
    }
    //guardar
    @PostMapping
    public ResponseEntity<RecompenzaDTO> guardar(@RequestBody RecompenzaDTO recompenzaDTO){
        return ResponseEntity.ok(recompenzaService.guardar(recompenzaDTO));
    }
    // Modificar
    @PutMapping("/{id}")
    public ResponseEntity<RecompenzaDTO> modificar(@PathVariable Long id, @RequestBody RecompenzaDTO dto) {
        RecompenzaDTO actualizado = recompenzaService.modificar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar lógico
    @DeleteMapping("/{id}")
    public ResponseEntity<RecompenzaDTO> eliminarLogico(@PathVariable Long id) {
        RecompenzaDTO eliminado = recompenzaService.eliminar(id);
        return ResponseEntity.ok(eliminado);
    }
}
