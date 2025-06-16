package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.EstudianteDTO;
import com.upc.campusflow.Exception.RecursoNoEncontradoException;
import com.upc.campusflow.Model.Carrera;
import com.upc.campusflow.Model.Estudiante;
import com.upc.campusflow.Model.EstudianteEstadistica;
import com.upc.campusflow.Model.Usuario;
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
        Estudiante estudiante = iEstudiante.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + id)); // Using custom exception
        if (estudiante == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(estudiante, EstudianteDTO.class);
    }

    // --- Listar Estudiantes ---
    public List<EstudianteDTO> listar() {
        ModelMapper modelMapper = new ModelMapper(); // Instancia local
        List<Estudiante> estudiantes = iEstudiante.findAll();
        return estudiantes.stream()
                .map(estudiante -> {
                    EstudianteDTO estudianteDTO = modelMapper.map(estudiante, EstudianteDTO.class);
                    // Poblar IDs de relaciones en el DTO para listar
                    if (estudiante.getCarrera() != null) {
                        estudianteDTO.setIdCarrera(estudiante.getCarrera().getIdCarrera());
                    }
                    if (estudiante.getUsuarios() != null) {
                        estudianteDTO.setIdUsuario(estudiante.getUsuarios().getIdUsuario());
                    }
                    return estudianteDTO;
                })
                .collect(Collectors.toList());
    }

    // --- Modificar Estudiante (AJUSTADO) ---
    public EstudianteDTO modificar(Long id, EstudianteDTO estudianteDTO) {
        ModelMapper modelMapper = new ModelMapper(); // Instancia local

        // 1. Buscar el estudiante existente
        Estudiante existente = iEstudiante.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + id));

        // **CAMBIO AQUÍ: Eliminamos la validación que impedía modificar estudiantes inactivos.**
        // Si necesitas una validación específica, como impedir cambiar el ciclo si está inactivo,
        // la pondrías para ese campo en particular, no para toda la operación.

        // 2. Actualizar campos básicos, incluyendo el estado
        existente.setCiclo(estudianteDTO.getCiclo());
        existente.setEstado(estudianteDTO.isEstado()); // <--- ¡Asegúrate de que este campo se actualiza!

        // 3. Actualizar Carrera (si se proporciona un nuevo ID de Carrera)
        if (estudianteDTO.getIdCarrera() != null) {
            if (existente.getCarrera() == null || !existente.getCarrera().getIdCarrera().equals(estudianteDTO.getIdCarrera())) {
                Carrera nuevaCarrera = iCarrera.findById(estudianteDTO.getIdCarrera())
                        .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con ID: " + estudianteDTO.getIdCarrera()));
                existente.setCarrera(nuevaCarrera);
            }
        } else {
            throw new IllegalArgumentException("El ID de carrera no puede ser nulo en la actualización.");
        }

        // 4. Actualizar Usuario (si se proporciona un nuevo ID de Usuario)
        if (estudianteDTO.getIdUsuario() != null) {
            if (existente.getUsuarios() == null || !existente.getUsuarios().getIdUsuario().equals(estudianteDTO.getIdUsuario())) {
                Usuario nuevoUsuario = iUsuario.findById(estudianteDTO.getIdUsuario())
                        .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + estudianteDTO.getIdUsuario()));
                existente.setUsuarios(nuevoUsuario);
            }
        } else {
            throw new IllegalArgumentException("El ID de usuario no puede ser nulo en la actualización.");
        }

        // 5. Guardar los cambios
        Estudiante actualizado = iEstudiante.save(existente);

        // 6. Mapear a DTO para la respuesta y poblar IDs de relaciones
        EstudianteDTO responseDto = modelMapper.map(actualizado, EstudianteDTO.class);
        responseDto.setIdCarrera(actualizado.getCarrera().getIdCarrera());
        responseDto.setIdUsuario(actualizado.getUsuarios().getIdUsuario());
        return responseDto;
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
