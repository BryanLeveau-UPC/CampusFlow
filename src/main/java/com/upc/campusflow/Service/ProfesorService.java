package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.NotaDTO;
import com.upc.campusflow.DTO.ProfesorDTO;
import com.upc.campusflow.Model.Nota;
import com.upc.campusflow.Model.Profesor;
import com.upc.campusflow.Model.Usuario;
import com.upc.campusflow.Repository.ProfesorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfesorService {
    final ProfesorRepository profesorRepository;

    public ProfesorService(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    //Listar
    public List<ProfesorDTO> listar(){

        List<Profesor> profesores = profesorRepository.findAll().stream().filter(Profesor::isEstado).toList();
        List<ProfesorDTO> profesorDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for(Profesor profesor : profesores){
            ProfesorDTO profesorDTO = modelMapper.map(profesor, ProfesorDTO.class);
            if(profesorDTO.getId_Usuario() != null){
                profesorDTO.setId_Usuario(profesor.getUsuario().getIdUsuario());
            }
            profesorDTOS.add(profesorDTO);
        }
        return profesorDTOS;
    }

    //Guardar
    public ProfesorDTO guardar(ProfesorDTO profesorDTO){
        ModelMapper modelMapper = new ModelMapper();
        Profesor profesor = modelMapper.map(profesorDTO, Profesor.class);
        if(profesorDTO.getId_Usuario()!= null){
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(profesorDTO.getId_Usuario());
            profesor.setUsuario(usuario);
        }
        profesor = profesorRepository.save(profesor);
        profesorDTO = modelMapper.map(profesor, ProfesorDTO.class);
        return profesorDTO;

    }

    //Modificar
    public ProfesorDTO modificar (Long id, ProfesorDTO profesorDTO){
        ModelMapper modelMapper = new ModelMapper();
        Profesor exits = profesorRepository.findById(id).orElseThrow(() -> new RuntimeException("Profesor no encontrado con ID: " + id));
        modelMapper.map(profesorDTO, exits);

        if(profesorDTO.getId_Usuario() != null){
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(profesorDTO.getId_Usuario());
            exits.setUsuario(usuario);
        }

        Profesor actualize = profesorRepository.save(exits);
        ProfesorDTO dto =  modelMapper.map(actualize, ProfesorDTO.class);
        return dto;
    }

    //Eliminar
    public ProfesorDTO eliminar (Long id){
        ModelMapper modelMapper = new ModelMapper();
        Profesor entidad = profesorRepository.findById(id).orElseThrow(() -> new RuntimeException("Profesor no encontrado con ID: " + id));
        entidad.setEstado(false);
        entidad =  profesorRepository.save(entidad);
        ProfesorDTO dto =  modelMapper.map(entidad, ProfesorDTO.class);
        return dto;
    }










}
