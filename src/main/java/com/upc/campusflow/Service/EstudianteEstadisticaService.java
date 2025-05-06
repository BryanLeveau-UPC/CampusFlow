package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.EstudianteEstadisticaDTO;
import com.upc.campusflow.Model.Estudiante;
import com.upc.campusflow.Model.EstudianteEstadistica;
import com.upc.campusflow.Repository.EstudianteEstadisticaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstudianteEstadisticaService {
    final EstudianteEstadisticaRepository estudianteEstadisticaRepository;

    public EstudianteEstadisticaService(EstudianteEstadisticaRepository estudianteEstadisticaRepository) {
        this.estudianteEstadisticaRepository = estudianteEstadisticaRepository;
    }
    //listar
    public List<EstudianteEstadisticaDTO> Listar(){
        List<EstudianteEstadistica> estudianteEstadisticas = estudianteEstadisticaRepository.findAll()            .stream()
                .filter(EstudianteEstadistica::isEstado) // Filtrar solo los activos
                .toList();
        List<EstudianteEstadisticaDTO> estudianteEstadisticaDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (EstudianteEstadistica estudianteEstadistica : estudianteEstadisticas) {
            EstudianteEstadisticaDTO estudianteEstadisticaDTO = modelMapper.map(estudianteEstadistica, EstudianteEstadisticaDTO.class);
            if(estudianteEstadistica.getEstudiante() != null){
                estudianteEstadisticaDTO.setId_Estudiante(estudianteEstadistica.getEstudiante().getIdEstudiante());
            }
            estudianteEstadisticaDTOS.add(estudianteEstadisticaDTO);
        }
        return estudianteEstadisticaDTOS;
    }
    //guardar

    public EstudianteEstadisticaDTO guardar(EstudianteEstadisticaDTO estudianteEstadisticaDTO) {
        ModelMapper modelMapper = new ModelMapper();
        EstudianteEstadistica estudianteEstadistica = modelMapper.map(estudianteEstadisticaDTO, EstudianteEstadistica.class);
        if (estudianteEstadisticaDTO.getId_Estudiante() != null) {
            Estudiante estudiante = new Estudiante();
            estudiante.setIdEstudiante(estudianteEstadisticaDTO.getId_Estudiante());
            estudianteEstadistica.setEstudiante(estudiante);
        }

        estudianteEstadistica = estudianteEstadisticaRepository.save(estudianteEstadistica);
        estudianteEstadisticaDTO = modelMapper.map(estudianteEstadistica, EstudianteEstadisticaDTO.class);
        return estudianteEstadisticaDTO;
    }
    //Modificar
    public EstudianteEstadisticaDTO modificar(Long id, EstudianteEstadisticaDTO dto) {
        EstudianteEstadistica existente = estudianteEstadisticaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estadística de estudiante no encontrada con ID: " + id));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(dto, existente); // Sobrescribe los datos existentes

        if (dto.getId_Estudiante() != null) {
            Estudiante estudiante = new Estudiante();
            estudiante.setIdEstudiante(dto.getId_Estudiante());
            existente.setEstudiante(estudiante);
        }

        EstudianteEstadistica actualizado = estudianteEstadisticaRepository.save(existente);
        return modelMapper.map(actualizado, EstudianteEstadisticaDTO.class);
    }
    //Eliminar lógico
    public void eliminarLogico(Long id) {
        EstudianteEstadistica entidad = estudianteEstadisticaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EstudianteEstadistica no encontrada con ID: " + id));
        entidad.setEstado(false); // ← Marcar como inactivo (eliminación lógica)
        estudianteEstadisticaRepository.save(entidad);
    }


    //Eliminar Físico
    public void eliminar(Long id) {
        if (!estudianteEstadisticaRepository.existsById(id)) {
            throw new RuntimeException("No se encontró EstudianteEstadistica con ID: " + id);
        }
        estudianteEstadisticaRepository.deleteById(id);
    }
}
