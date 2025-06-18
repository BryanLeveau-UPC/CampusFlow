package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.ProfesorDTO;
import com.upc.campusflow.Exception.RecursoNoEncontradoException;
import com.upc.campusflow.Model.Profesor;
import com.upc.campusflow.Model.Usuario;
import com.upc.campusflow.Repository.ProfesorRepository;
import com.upc.campusflow.Repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
        // 1. Retrieve the existing entity
        Profesor existingProfesor = profesorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Profesor no encontrado con ID: " + id));

        // --- VALIDATIONS ---
        // Ensure Usuario ID is provided for modification
        if (profesorDTO.getUsuario() == null) {
            throw new IllegalArgumentException("El ID de usuario es requerido para modificar un profesor.");
        }

        // Fetch the Usuario object
        Usuario usuario = usuarioRepository.findById(profesorDTO.getUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario con ID " + profesorDTO.getUsuario() + " no encontrado."
                ));

        // --- END VALIDATIONS ---

        // 2. Configure ModelMapper for updating the existing entity and for returning the DTO
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Mappings for DTO to existing Entity
        modelMapper.createTypeMap(ProfesorDTO.class, Profesor.class)
                .addMappings(mapper -> {
                    mapper.skip(Profesor::setIdProfesor); // CRUCIAL: Skip mapping the ID of the Profesor itself
                    mapper.skip(Profesor::setUsuario);    // CRUCIAL: Skip mapping the Usuario object (we will set it manually)
                });

        // Mappings for Entity to DTO for response (can be part of the same ModelMapper instance)
        modelMapper.createTypeMap(Profesor.class, ProfesorDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getUsuario().getIdUsuario(), ProfesorDTO::setUsuario);
                });


        // 3. Map relevant fields from DTO to the existing entity
        // Make sure to use the configured modelMapper instance
        modelMapper.map(profesorDTO, existingProfesor);

        // 4. Manually set the Usuario object
        existingProfesor.setUsuario(usuario);

        // 5. Explicitly handle the 'Estado' field
        existingProfesor.setEstado(profesorDTO.isEstado()); // Assuming 'Estado' will always be present in DTO for PUT

        // 6. Save the updated entity
        Profesor updatedProfesor = profesorRepository.save(existingProfesor);

        // 7. Map the updated entity back to DTO for response using the *same* configured ModelMapper
        ProfesorDTO dto = modelMapper.map(updatedProfesor, ProfesorDTO.class);

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
