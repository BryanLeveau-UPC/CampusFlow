package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.EstudianteDTO;
import com.upc.campusflow.Model.Carrera;
import com.upc.campusflow.Model.Estudiante;
import com.upc.campusflow.Model.EstudianteEstadistica;
import com.upc.campusflow.Repository.CarreraRepository;
import com.upc.campusflow.Repository.EstudianteRepository;
import com.upc.campusflow.Repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class EstudianteService {

    final EstudianteRepository iEstudiante;
    final CarreraRepository iCarrera;
    final UsuarioRepository iUsuario;

    public EstudianteService(EstudianteRepository iEstudiante, CarreraRepository iCarrera, UsuarioRepository iUsuario) {
        this.iEstudiante = iEstudiante;
        this.iCarrera = iCarrera;
        this.iUsuario = iUsuario;
    }

    public EstudianteDTO guardar(EstudianteDTO estudianteDTO){
        ModelMapper modelMapper = new ModelMapper();
        Estudiante estudiante = modelMapper.map(estudianteDTO, Estudiante.class);

        // --- Manejo de la Asociación con Carrera ---
        if (estudianteDTO.getIdCarrera() != null) {
            iCarrera.findById(estudianteDTO.getIdCarrera())
                    .ifPresent(estudiante::setCarrera);
        } else {
            throw new RuntimeException("El ID de carrera no puede ser nulo al registrar un estudiante.");
        }

        // --- Manejo de la Asociación con Usuario ---
        if (estudianteDTO.getIdUsuario() != null) {
            iUsuario.findById(estudianteDTO.getIdUsuario())
                    .ifPresent(estudiante::setUsuarios);
        } else {
            throw new RuntimeException("El ID de usuario no puede ser nulo al registrar un estudiante.");
        }

        if (estudiante.getUsuarios() == null) {
            throw new RuntimeException("No se encontró el Usuario con ID: " + estudianteDTO.getIdUsuario() + ". No se puede registrar el estudiante.");
        }

        // Guarda el Estudiante.
        estudiante = iEstudiante.save(estudiante);

        // --- ¡CAMBIO CLAVE AQUÍ PARA EL MAPEO DE RESPUESTA SIN PROPERTYMAP! ---
        EstudianteDTO responseDto = modelMapper.map(estudiante, EstudianteDTO.class);

        // Asignación manual del idUsuario después del mapeo base
        if (estudiante.getUsuarios() != null) {
            responseDto.setIdUsuario(estudiante.getUsuarios().getIdUsuario());
        }
        // No necesitas un 'else' aquí, ya que si es null, el idUsuario en el DTO se mantendrá null (que es el valor por defecto o lo que vino del input si no se sobreescribe)
        // pero con la validación anterior, 'estudiante.getUsuarios()' nunca debería ser null en este punto.

        return responseDto;
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

        if (estudianteDTO.getIdCarrera() != null) {
            Carrera carrera = new Carrera();
            carrera.setIdCarrera(estudianteDTO.getIdCarrera());
            existente.setCarrera(carrera);
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


    public List<EstudianteDTO> obtenerEstudiantesConNotaBaja() {
        List<Estudiante> estudiantes = iEstudiante.findEstudiantesConNotaMenorA11();
        ModelMapper modelMapper = new ModelMapper();
        List<EstudianteDTO> dtoList = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            dtoList.add(modelMapper.map(e, EstudianteDTO.class));
        }
        return dtoList;
    }


    /**
     * Devuelve el top 10% de estudiantes según promedio de notas.
     */
    private final ModelMapper mapper = new ModelMapper();

    public List<EstudianteDTO> topDecilePorNota() {
        long total = iEstudiante.count();
        int decileCount = (int) Math.ceil(total * 0.1);
        if (decileCount < 1) decileCount = 1;

        // Página única con tamaño = decileCount
        List<Estudiante> top = iEstudiante
                .findAllOrderByAverageNotaDesc(PageRequest.of(0, decileCount));

        return top.stream()
                .map(e -> mapper.map(e, EstudianteDTO.class))
                .toList();
    }

    public List<EstudianteDTO> obtenerEstudiantesPorRangoDeFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Estudiante> estudiantes = iEstudiante.obtenerEstudiantesPorRangoDeFechas(fechaInicio, fechaFin);
        ModelMapper modelMapper = new ModelMapper();
        return estudiantes.stream()
                .map(e -> modelMapper.map(e, EstudianteDTO.class))
                .collect(Collectors.toList());
    }
}
