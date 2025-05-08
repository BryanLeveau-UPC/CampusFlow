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
}
