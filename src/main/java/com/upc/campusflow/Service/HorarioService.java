package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.HorarioDTO;
import com.upc.campusflow.Model.Horario;
import com.upc.campusflow.Model.Horario;
import com.upc.campusflow.Repository.HorarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HorarioService {
    final HorarioRepository horarioRepository;

    public HorarioService(HorarioRepository horarioRepository) {
        this.horarioRepository = horarioRepository;
    }

    //Listar
    public List<HorarioDTO> listar(){
        List<Horario> horarios = horarioRepository.findAll().stream().filter(Horario::isEstado).toList();;
        List<HorarioDTO> usuarioDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        for(Horario usuario : horarios){
            HorarioDTO usuarioDTO = modelMapper.map(usuario, HorarioDTO.class);
            usuarioDTOS.add(usuarioDTO);
        }
        return usuarioDTOS;
    }

    //Guardar
    public HorarioDTO guardar(HorarioDTO usuarioDTO){
        ModelMapper modelMapper = new ModelMapper();
        Horario usuario = modelMapper.map(usuarioDTO, Horario.class);
        usuario = horarioRepository.save(usuario);
        usuarioDTO = modelMapper.map(usuario, HorarioDTO.class);
        return usuarioDTO;
    }

    //Modificar
    public HorarioDTO modificar (Long id, HorarioDTO usuarioDTO){
        ModelMapper modelMapper = new ModelMapper();
        Horario exits = horarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        modelMapper.map(usuarioDTO, exits);

        Horario actualize = horarioRepository.save(exits);
        HorarioDTO dto = modelMapper.map(actualize, HorarioDTO.class);
        return dto;
    }

    //Eliminar
    public HorarioDTO eliminar (Long id){
        ModelMapper modelMapper = new ModelMapper();
        Horario entidad = horarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        entidad.setEstado(false);
        entidad = horarioRepository.save(entidad);
        HorarioDTO dto = modelMapper.map(entidad, HorarioDTO.class);
        return dto;
    }

}
