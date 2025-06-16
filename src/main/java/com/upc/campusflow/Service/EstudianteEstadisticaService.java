package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.EstudianteEstadisticaDTO;
import com.upc.campusflow.Exception.RecursoNoEncontradoException;
import com.upc.campusflow.Model.Estudiante;
import com.upc.campusflow.Model.EstudianteEstadistica;
import com.upc.campusflow.Repository.EstudianteEstadisticaRepository;
import com.upc.campusflow.Repository.EstudianteRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstudianteEstadisticaService {
    final EstudianteEstadisticaRepository estudianteEstadisticaRepository;
    final EstudianteRepository estudianteRepository;

    public EstudianteEstadisticaService(EstudianteEstadisticaRepository estudianteEstadisticaRepository, EstudianteRepository estudianteRepository) {
        this.estudianteEstadisticaRepository = estudianteEstadisticaRepository;
        this.estudianteRepository = estudianteRepository;
    }
    //listar
    public List<EstudianteEstadisticaDTO> Listar(){
        List<EstudianteEstadistica> estudianteEstadisticas = estudianteEstadisticaRepository.findAll()
                .stream()
                .filter(EstudianteEstadistica::isEstado) // Filtrar solo los activos
                .toList();

        List<EstudianteEstadisticaDTO> estudianteEstadisticaDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        // Configuración para mapear de Entidad a DTO (para que Id_Estudiante se rellene)
        modelMapper.createTypeMap(EstudianteEstadistica.class, EstudianteEstadisticaDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getEstudiante().getIdEstudiante(), EstudianteEstadisticaDTO::setId_Estudiante);
                });

        for (EstudianteEstadistica estudianteEstadistica : estudianteEstadisticas) {
            EstudianteEstadisticaDTO estudianteEstadisticaDTO = modelMapper.map(estudianteEstadistica, EstudianteEstadisticaDTO.class);
            // La siguiente línea ya no es necesaria si el TypeMap de arriba funciona
            // if(estudianteEstadistica.getEstudiante() != null){
            //     estudianteEstadisticaDTO.setId_Estudiante(estudianteEstadistica.getEstudiante().getIdEstudiante());
            // }
            estudianteEstadisticaDTOS.add(estudianteEstadisticaDTO);
        }
        return estudianteEstadisticaDTOS;
    }

    //guardar
    public EstudianteEstadisticaDTO guardar(EstudianteEstadisticaDTO estudianteEstadisticaDTO) {
        // --- VALIDACIONES ---
        if (estudianteEstadisticaDTO.getId_Estudiante() == null) {
            throw new IllegalArgumentException("El ID del estudiante es requerido para guardar una estadística.");
        }

        Estudiante estudiante = estudianteRepository.findById(estudianteEstadisticaDTO.getId_Estudiante())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Estudiante con ID " + estudianteEstadisticaDTO.getId_Estudiante() + " no encontrado."
                ));
        // --- FIN VALIDACIONES ---

        ModelMapper modelMapper = new ModelMapper();
        // Configuración para mapear de DTO a Entidad
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // Buena práctica
        modelMapper.createTypeMap(EstudianteEstadisticaDTO.class, EstudianteEstadistica.class)
                .addMappings(mapper -> {
                    // **CRUCIAL**: Ignorar el mapeo automático del objeto Estudiante anidado
                    mapper.skip(EstudianteEstadistica::setEstudiante);
                });

        EstudianteEstadistica estudianteEstadistica = modelMapper.map(estudianteEstadisticaDTO, EstudianteEstadistica.class);

        // Asigna el objeto Estudiante recuperado manualmente
        estudianteEstadistica.setEstudiante(estudiante);


        estudianteEstadistica = estudianteEstadisticaRepository.save(estudianteEstadistica);

        // Configuración para mapear de Entidad a DTO (para el retorno)
        ModelMapper returnModelMapper = new ModelMapper(); // Necesitas una nueva instancia para el mapeo de retorno
        returnModelMapper.createTypeMap(EstudianteEstadistica.class, EstudianteEstadisticaDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getEstudiante().getIdEstudiante(), EstudianteEstadisticaDTO::setId_Estudiante);
                });

        return returnModelMapper.map(estudianteEstadistica, EstudianteEstadisticaDTO.class);
    }

    //Modificar
    public EstudianteEstadisticaDTO modificar(Long id, EstudianteEstadisticaDTO estudianteEstadisticaDTO) {
        EstudianteEstadistica existente = estudianteEstadisticaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estadística de estudiante no encontrada con ID: " + id));

        // --- VALIDACIONES ---
        if (estudianteEstadisticaDTO.getId_Estudiante() == null) {
            throw new IllegalArgumentException("El ID del estudiante es requerido para modificar una estadística.");
        }

        Estudiante estudiante = estudianteRepository.findById(estudianteEstadisticaDTO.getId_Estudiante())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Estudiante con ID " + estudianteEstadisticaDTO.getId_Estudiante() + " no encontrado."
                ));
        // --- FIN VALIDACIONES ---

        ModelMapper modelMapper = new ModelMapper();
        // Configuración para mapear de DTO a Entidad existente
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // Buena práctica
        modelMapper.createTypeMap(EstudianteEstadisticaDTO.class, EstudianteEstadistica.class)
                .addMappings(mapper -> {
                    // **CRUCIAL**: Ignorar el mapeo automático del objeto Estudiante anidado
                    mapper.skip(EstudianteEstadistica::setEstudiante);
                });

        modelMapper.map(estudianteEstadisticaDTO, existente); // Sobrescribe los datos existentes

        // Asigna el objeto Estudiante recuperado manualmente
        existente.setEstudiante(estudiante);

        EstudianteEstadistica actualizado = estudianteEstadisticaRepository.save(existente);

        // Configuración para mapear de Entidad a DTO (para el retorno)
        ModelMapper returnModelMapper = new ModelMapper(); // Necesitas una nueva instancia para el mapeo de retorno
        returnModelMapper.createTypeMap(EstudianteEstadistica.class, EstudianteEstadisticaDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getEstudiante().getIdEstudiante(), EstudianteEstadisticaDTO::setId_Estudiante);
                });

        return returnModelMapper.map(actualizado, EstudianteEstadisticaDTO.class);
    }

    //Eliminar lógico
    public EstudianteEstadisticaDTO eliminar(Long id) {
        EstudianteEstadistica entidad = estudianteEstadisticaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("EstudianteEstadistica no encontrada con ID: " + id));
        entidad.setEstado(false);
        entidad =  estudianteEstadisticaRepository.save(entidad);

        ModelMapper modelMapper = new ModelMapper();
        // Configuración para mapear de Entidad a DTO (para el retorno)
        modelMapper.createTypeMap(EstudianteEstadistica.class, EstudianteEstadisticaDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getEstudiante().getIdEstudiante(), EstudianteEstadisticaDTO::setId_Estudiante);
                });

        return modelMapper.map(entidad, EstudianteEstadisticaDTO.class);
    }


}