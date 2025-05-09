package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.EstudianteDTO;
import com.upc.campusflow.Model.Carrera;
import com.upc.campusflow.Model.Estudiante;
import com.upc.campusflow.Model.EstudianteEstadistica;
import com.upc.campusflow.Repository.EstudianteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    public EstudianteDTO modificar(Long id, EstudianteDTO estudianteDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Estudiante existente = iEstudiante.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + id));

        if (!existente.isEstado()) {
            throw new RuntimeException("No se puede modificar un estudiante inactivo.");
        }

        existente.setCiclo(estudianteDTO.getCiclo());

        if (estudianteDTO.getIdCarreras() != null) {
            Carrera carrera = new Carrera();
            carrera.setIdCarrera(estudianteDTO.getIdCarreras());
            existente.setCarrera(carrera);
        }

        if (estudianteDTO.getEstudianteEstadistica() != null) {
            EstudianteEstadistica estadistica = new EstudianteEstadistica();
            estadistica.setIdEstudianteEstadistica(estudianteDTO.getEstudianteEstadistica());
            existente.setEstudianteEstadistica(estadistica);
        }

        Estudiante actualizado = iEstudiante.save(existente);
        return modelMapper.map(actualizado, EstudianteDTO.class);
    }

    // Eliminar lógico
    public EstudianteDTO eliminar(Long id) {
        ModelMapper modelMapper = new ModelMapper();
        Estudiante entidad = iEstudiante.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + id));
        entidad.setEstado(false);
        entidad = iEstudiante.save(entidad);
        return modelMapper.map(entidad, EstudianteDTO.class);
    }

    public Map<Integer, Long> resumenActivosPorCiclo() {
        return iEstudiante
                .countActiveByCiclo()
                .stream()
                .collect(Collectors.toMap(
                        fila -> (Integer) fila[0],
                        fila -> (Long)    fila[1]
                ));
    }


    public List<EstudianteDTO> obtenerEstudiantesConNotaBaja() {
        List<Estudiante> estudiantes = iEstudiante.findEstudiantesConNotaMenorA11();
        ModelMapper modelMapper = new ModelMapper();
        List<EstudianteDTO> dtoList = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            dtoList.add(modelMapper.map(e, EstudianteDTO.class));
        }
        return dtoList;
    }
}
