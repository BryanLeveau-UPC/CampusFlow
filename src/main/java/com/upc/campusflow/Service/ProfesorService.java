package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.ProfesorDTO;
import com.upc.campusflow.Model.Profesor;
import com.upc.campusflow.Model.Usuario;
import com.upc.campusflow.Repository.ProfesorRepository;
import com.upc.campusflow.Repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfesorService {
    final ProfesorRepository profesorRepository;
    final UsuarioRepository usuarioRepository;

    public ProfesorService(ProfesorRepository profesorRepository, UsuarioRepository usuarioRepository) {
        this.profesorRepository = profesorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    //Listar
    public List<ProfesorDTO> listar(){

        List<Profesor> profesores = profesorRepository.findAll().stream().filter(Profesor::isEstado).toList();
        List<ProfesorDTO> profesorDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for(Profesor profesor : profesores){
            ProfesorDTO profesorDTO = modelMapper.map(profesor, ProfesorDTO.class);
            profesorDTOS.add(profesorDTO);
        }
        return profesorDTOS;
    }

    //Guardar
    public ProfesorDTO guardar(ProfesorDTO profesorDTO){
        ModelMapper modelMapper = new ModelMapper(); // Instancia el ModelMapper aquí
        Profesor profesor = modelMapper.map(profesorDTO, Profesor.class);

        // Fetch the Usuario using the provided usuario ID from DTO
        if (profesorDTO.getUsuario() != null) {
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(profesorDTO.getUsuario());
            if (optionalUsuario.isPresent()) {
                profesor.setUsuario(optionalUsuario.get());
            } else {
                throw new RuntimeException("No se encontró el Usuario con ID: " + profesorDTO.getUsuario() + ". No se puede registrar el profesor.");
            }
        } else {
            throw new RuntimeException("El ID de usuario no puede ser nulo al registrar un profesor.");
        }

        // Antes de guardar, asegurémonos de que el objeto Usuario NO es nulo
        if (profesor.getUsuario() == null) {
            throw new RuntimeException("Error interno: El objeto Usuario no pudo ser asignado al Profesor antes de guardar.");
        }

        profesor = profesorRepository.save(profesor);

        // --- Asignación manual del ID de Usuario para la respuesta (SIN PROPERTYMAP) ---
        ProfesorDTO dto = modelMapper.map(profesor, ProfesorDTO.class);
        if (profesor.getUsuario() != null) {
            dto.setUsuario(profesor.getUsuario().getIdUsuario());
        }
        // --- Fin de la asignación manual ---

        return dto;
    }

    //Modificar
    public ProfesorDTO modificar (Long id, ProfesorDTO profesorDTO){
        ModelMapper modelMapper = new ModelMapper();
        Profesor exits = profesorRepository.findById(id).orElseThrow(() -> new RuntimeException("Profesor no encontrado con ID: " + id));
        modelMapper.map(profesorDTO, exits);

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
