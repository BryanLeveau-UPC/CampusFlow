package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.EstudianteDTO;
import com.upc.campusflow.Model.Estudiante;
import com.upc.campusflow.Repository.EstudianteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class EstudianteService {

    final EstudianteRepository iEstudiante;

    public EstudianteService(EstudianteRepository iEstudiante) {
        this.iEstudiante = iEstudiante;
    }

    public EstudianteDTO guardar(EstudianteDTO estudianteDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Estudiante estudiante = modelMapper.map(estudianteDTO, Estudiante.class);
        estudiante = iEstudiante.save(estudiante);
        return modelMapper.map(estudiante, EstudianteDTO.class);
    }

    public EstudianteDTO buscarPorId(Long id) {
        Estudiante estudiante = iEstudiante.findById(id).orElse(null);
        if (estudiante == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(estudiante, EstudianteDTO.class);
    }

    public List<EstudianteDTO> listar() {
        List<Estudiante> estudiantes = iEstudiante.findAll();
        List<EstudianteDTO> estudianteDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Estudiante estudiante : estudiantes) {
            EstudianteDTO estudianteDTO = modelMapper.map(estudiante, EstudianteDTO.class);
            estudianteDTOS.add(estudianteDTO);
        }
        return estudianteDTOS;
    }
}
