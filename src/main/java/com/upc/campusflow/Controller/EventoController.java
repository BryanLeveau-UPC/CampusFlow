package com.upc.campusflow.Controller;

import com.upc.campusflow.DTO.EventoDTO;
import com.upc.campusflow.Service.EventoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;
@CrossOrigin

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
//QUERY: LISTAR EVENTOS POR PROFESOR
    @GetMapping("/profesor/{id}")
    public ResponseEntity<List<EventoDTO>> listarPorProfesor(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.listarPorProfesor(id));
    }

    /**QUERY: Proximos5eventosDeEstudiante
    @GetMapping("/estudiante/{id}/proximos5")*/
    public ResponseEntity<List<EventoDTO>> proximos5(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.proximos5EventosDeEstudiante(id));
    }

    /**
     * GET /eventos/top3/participacion
     * Devuelve los 3 eventos con más estudiantes inscritos.
     */
    @GetMapping("/top3/participacion")
    public ResponseEntity<List<EventoDTO>> top3Participacion() {
        return ResponseEntity.ok(eventoService.top3EventosMasParticipacion());
    }

}
