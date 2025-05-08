package com.upc.campusflow.Service;
import com.upc.campusflow.DTO.EventoDTO;
import com.upc.campusflow.Model.Evento;
import com.upc.campusflow.Model.Profesor;
import com.upc.campusflow.Repository.EventoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventoService {

    final EventoRepository iEventoRepository;

    public EventoService(EventoRepository iEventoRepository) {
        this.iEventoRepository = iEventoRepository;
    }

    public EventoDTO guardar(EventoDTO eventoDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Evento evento = modelMapper.map(eventoDTO, Evento.class);


        if (eventoDTO.getIdProfe() != null) {
            Profesor profesor = new Profesor();
            profesor.setIdProfesor(eventoDTO.getId());
            evento.setIdProfe(profesor);
        }

        evento = iEventoRepository.save(evento);
        return modelMapper.map(evento, EventoDTO.class);
    }

    public EventoDTO buscarPorId(Long id) {
        Evento evento = iEventoRepository.findById(id).orElse(null);
        if (evento == null) return null;

        ModelMapper modelMapper = new ModelMapper();
        EventoDTO eventoDTO = modelMapper.map(evento, EventoDTO.class);
        if (evento.getIdProfe() != null) {
            eventoDTO.setId(evento.getIdProfe().getIdProfesor());
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
        ModelMapper modelMapper = new ModelMapper();
        Evento existente = iEventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));

        if (!existente.isEstado()) {
            throw new RuntimeException("No se puede modificar un evento inactivo.");
        }

        modelMapper.map(eventoDTO, existente); // Copia los nuevos datos

        if (eventoDTO.getIdProfe() != null) {
            Profesor profe = new Profesor();
            profe.setIdProfesor(eventoDTO.getId());
            existente.setIdProfe(profe);
        }

        Evento actualizado = iEventoRepository.save(existente);
        return modelMapper.map(actualizado, EventoDTO.class);
    }

    // Eliminación lógica
    public EventoDTO eliminar(Long id) {
        ModelMapper modelMapper = new ModelMapper();
        Evento evento = iEventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));

        evento.setEstado(false);
        evento = iEventoRepository.save(evento);
        return modelMapper.map(evento, EventoDTO.class);
    }



}
