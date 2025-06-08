package com.upc.campusflow.Controller;

import com.upc.campusflow.DTO.UsuarioDTO;
import com.upc.campusflow.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    //listar
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> Listar(){
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    //guardar
    @PostMapping
    public ResponseEntity<UsuarioDTO> guardar(@RequestBody UsuarioDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.crearUsuario(usuarioDTO));
    }



    // Modificar
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> modificar(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, usuarioDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}