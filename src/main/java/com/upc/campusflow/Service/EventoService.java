package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.EventoDTO;
import com.upc.campusflow.Exception.RecursoNoEncontradoException;
import com.upc.campusflow.Model.Evento;
import com.upc.campusflow.Model.Profesor;
import com.upc.campusflow.Repository.EventoRepository;
import com.upc.campusflow.Repository.ProfesorRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventoService {

    final EventoRepository iEventoRepository;
    final ProfesorRepository iProfesorRepository;

    public EventoService(EventoRepository iEventoRepository, ProfesorRepository iProfesorRepository) {
        this.iEventoRepository = iEventoRepository;
        this.iProfesorRepository = iProfesorRepository;
    }


    public EventoDTO guardar(EventoDTO eventoDTO) {
        ModelMapper modelMapper = new ModelMapper(); // ModelMapper instance for this method
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Configure DTO to Entity mapping for saving
        modelMapper.createTypeMap(EventoDTO.class, Evento.class)
                .addMappings(mapper -> {
                    mapper.skip(Evento::setIdEvento); // Skip ID as it's auto-generated
                    mapper.skip(Evento::setProfesor); // Skip Profesor object, will be set manually
                    mapper.skip(Evento::setEstudiantes); // Skip students list, if not handled in DTO
                });

        // --- Validations for saving ---
        if (eventoDTO.getIdProfesor() == null) {
            throw new IllegalArgumentException("El ID del profesor es requerido para crear un evento.");
        }
        Profesor profesor = iProfesorRepository.findById(eventoDTO.getIdProfesor())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Profesor con ID " + eventoDTO.getIdProfesor() + " no encontrado."
                ));

        // Date Validations
        if (eventoDTO.getFechaInicio() == null || eventoDTO.getFechaFin() == null) {
            throw new IllegalArgumentException("Fecha de inicio y Fecha de fin son requeridas.");
        }
        if (eventoDTO.getFechaFin().isBefore(eventoDTO.getFechaInicio())) {
            throw new IllegalArgumentException("La Fecha de Fin no puede ser anterior a la Fecha de Inicio.");
        }
        if (eventoDTO.getFechaInicio().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La Fecha de Inicio no puede ser una fecha pasada.");
        }
        // --- End Validations ---

        Evento evento = modelMapper.map(eventoDTO, Evento.class);
        evento.setProfesor(profesor); // Set the actual Profesor entity

        evento = iEventoRepository.save(evento);

        // Configure Entity to DTO mapping for the return value
        ModelMapper returnMapper = new ModelMapper(); // New ModelMapper for return DTO mapping
        returnMapper.createTypeMap(Evento.class, EventoDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getProfesor() != null ? src.getProfesor().getIdProfesor() : null, EventoDTO::setIdProfesor);
                });
        return returnMapper.map(evento, EventoDTO.class);
    }

    public EventoDTO buscarPorId(Long id) {
        Evento evento = iEventoRepository.findById(id).orElse(null);
        if (evento == null) return null;

        ModelMapper modelMapper = new ModelMapper();
        EventoDTO eventoDTO = modelMapper.map(evento, EventoDTO.class);
        if (evento.getProfesor() != null) {
            eventoDTO.setIdProfesor(evento.getProfesor().getIdProfesor());
        }
        return eventoDTO;
    }

    public List<EventoDTO> listar() {
        List<Evento> eventos = iEventoRepository.findAll();
        List<EventoDTO> eventoDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Evento evento : eventos) {
            EventoDTO dto = modelMapper.map(evento, EventoDTO.class);
            eventoDTOS.add(dto);
        }
        return eventoDTOS;
    }

    public EventoDTO modificar(Long id, EventoDTO eventoDTO) {
        Evento existingEvento = iEventoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Evento no encontrado con ID: " + id));

        // --- Business Logic Validation ---
        if (!existingEvento.isEstado() && !eventoDTO.isEstado()) {
            throw new IllegalArgumentException("No se puede modificar un evento inactivo (estado=false). Para modificarlo, debe cambiar su estado a activo (estado=true) en la petición.");
        }

        // --- Validations ---
        if (eventoDTO.getIdProfesor() == null) {
            throw new IllegalArgumentException("El ID del profesor es requerido para modificar un evento.");
        }
        Profesor profesor = iProfesorRepository.findById(eventoDTO.getIdProfesor())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Profesor con ID " + eventoDTO.getIdProfesor() + " no encontrado."
                ));

        if (eventoDTO.getFechaInicio() == null || eventoDTO.getFechaFin() == null) {
            throw new IllegalArgumentException("Fecha de inicio y Fecha de fin son requeridas.");
        }
        if (eventoDTO.getFechaFin().isBefore(eventoDTO.getFechaInicio())) {
            throw new IllegalArgumentException("La Fecha de Fin no puede ser anterior a la Fecha de Inicio.");
        }
        // --- End Validations ---

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Configure DTO to existing Entity mapping
        modelMapper.createTypeMap(EventoDTO.class, Evento.class)
                .addMappings(mapper -> {
                    mapper.skip(Evento::setIdEvento); // Skip ID as it's for an existing entity
                    mapper.skip(Evento::setProfesor); // Profesor will be set manually
                    mapper.skip(Evento::setEstudiantes); // Students list is not directly mapped from DTO
                });

        modelMapper.map(eventoDTO, existingEvento);

        existingEvento.setProfesor(profesor);
        existingEvento.setEstado(eventoDTO.isEstado());

        Evento updatedEvento = iEventoRepository.save(existingEvento);

        // Manually map to DTO for the return value
        EventoDTO returnDTO = modelMapper.map(updatedEvento, EventoDTO.class);
        if (updatedEvento.getProfesor() != null) {
            returnDTO.setIdProfesor(updatedEvento.getProfesor().getIdProfesor());
        }
        return returnDTO;
    }

    public EventoDTO eliminar(Long id) {
        ModelMapper modelMapper = new ModelMapper();
        Evento evento = iEventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));

        evento.setEstado(false);
        evento = iEventoRepository.save(evento);
        return modelMapper.map(evento, EventoDTO.class);
    }

    public List<EventoDTO> listarPorProfesor(Long idProfesor) {
        List<Evento> eventos = iEventoRepository.findEventosByProfesorId(idProfesor);
        List<EventoDTO> eventoDTOs = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Evento evento : eventos) {
            EventoDTO dto = modelMapper.map(evento, EventoDTO.class);
            dto.setIdProfesor(evento.getProfesor().getIdProfesor());
            eventoDTOs.add(dto);
        }
        return eventoDTOs;
    }

    public List<EventoDTO> proximos5EventosDeEstudiante(Long idEstudiante) {
        List<Evento> eventos = iEventoRepository.findNext5EventosByEstudiante(
                idEstudiante,
                LocalDate.now(),
                PageRequest.of(0, 5)
        );
        List<EventoDTO> eventosDTO = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Evento evento : eventos) {
            eventosDTO.add(modelMapper.map(evento, EventoDTO.class));
        }
        return eventosDTO;
    }


    private final ModelMapper mapper = new ModelMapper();
    /**
     * Devuelve los 3 eventos en los que más estudiantes participaron
     */
    public List<EventoDTO> top3EventosMasParticipacion() {
        var eventos = iEventoRepository.findTop3EventosByParticipacion(PageRequest.of(0, 3));
        List<EventoDTO> dtos = new ArrayList<>();
        for (Evento ev : eventos) {
            dtos.add(mapper.map(ev, EventoDTO.class));
        }
        return dtos;
    }
}