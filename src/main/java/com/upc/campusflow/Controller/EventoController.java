package com.upc.campusflow.Controller;

import com.upc.campusflow.DTO.EventoDTO;
import com.upc.campusflow.Service.EventoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/Evento")
public class EventoController {

    final EventoService eventoService;

    Logger log = Logger.getLogger(EventoController.class.getName());

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    public ResponseEntity<List<EventoDTO>> listar() {
        return ResponseEntity.ok(eventoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO> buscarPorId(@PathVariable Long id) {
        EventoDTO eventoDTO = eventoService.buscarPorId(id);
        if (eventoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventoDTO);
    }

    @PostMapping
    public ResponseEntity<EventoDTO> guardar(@RequestBody EventoDTO eventoDTO) {
        log.info("Guardando evento: " + eventoDTO.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.guardar(eventoDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoDTO> modificar(@PathVariable Long id, @RequestBody EventoDTO eventoDTO) {
        EventoDTO actualizado = eventoService.modificar(id, eventoDTO);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EventoDTO> eliminar(@PathVariable Long id) {
        EventoDTO eliminado = eventoService.eliminar(id);
        return ResponseEntity.ok(eliminado);
    }
}
