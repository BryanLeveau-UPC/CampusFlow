package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.UsuarioDTO;
import com.upc.campusflow.Model.Rol;
import com.upc.campusflow.Model.Usuario;
import com.upc.campusflow.Repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {
    final UsuarioRepository usuarioRepository;
    final RolService rolService;

    public UsuarioService(UsuarioRepository usuarioRepository, RolService rolService) {
        this.usuarioRepository = usuarioRepository;
        this.rolService = rolService;
    }

    //Listar
    public List<UsuarioDTO> listar(){
        List<Usuario> usuarios = usuarioRepository.findAll().stream().filter(Usuario::isEstado).toList();
        List<UsuarioDTO> usuarioDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        for(Usuario usuario : usuarios){
            UsuarioDTO usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
            usuarioDTOS.add(usuarioDTO);
        }
        return usuarioDTOS;
    }

    // Guardar usuario
    public UsuarioDTO guardar(UsuarioDTO usuarioDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);

        // Buscar el rol por ID
        Rol rol = rolService.buscarRolPorId(usuarioDTO.getRolId());
        if (rol == null) {
            throw new RuntimeException("Rol no encontrado con ID: " + usuarioDTO.getRolId());
        }

        // Asignar el rol único al usuario
        usuario.setRol(rol);
        usuario.setEstado(true);

        Usuario savedUsuario = usuarioRepository.save(usuario);
        UsuarioDTO dto = modelMapper.map(savedUsuario, UsuarioDTO.class);
        return dto;
    }

    //Modificar
    public UsuarioDTO modificar (Long id, UsuarioDTO usuarioDTO){
        ModelMapper modelMapper = new ModelMapper();
        Usuario exits = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        modelMapper.map(usuarioDTO, exits);

        Usuario actualize = usuarioRepository.save(exits);
        UsuarioDTO dto = modelMapper.map(actualize, UsuarioDTO.class);
        return dto;
    }

    //Eliminar
    public UsuarioDTO eliminar (Long id){
        ModelMapper modelMapper = new ModelMapper();
        Usuario entidad = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        entidad.setEstado(false);
        entidad = usuarioRepository.save(entidad);
        UsuarioDTO dto = modelMapper.map(entidad, UsuarioDTO.class);
        return dto;
    }

}
