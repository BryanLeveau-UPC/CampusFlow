package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.RecursoDTO;
import com.upc.campusflow.Model.Recurso;
import com.upc.campusflow.Repository.RecursoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecursoService {
    final RecursoRepository recursoRepository;

    public RecursoService(RecursoRepository recursoRepository) {
        this.recursoRepository = recursoRepository;
    }
    // Listar todos los recursos
    public List<RecursoDTO> listar() {
        List<Recurso> recursos = recursoRepository.findAll();
        List<RecursoDTO> recursosDTO = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Recurso recurso : recursos) {
            RecursoDTO recursoDTO = modelMapper.map(recurso, RecursoDTO.class);
            recursosDTO.add(recursoDTO);
        }
        return recursosDTO;
    }

    // Buscar recurso por ID
    public RecursoDTO buscarPorId(int idRecurso) {
        Recurso recurso = recursoRepository.findById(idRecurso).get();
        ModelMapper modelMapper = new ModelMapper();
        RecursoDTO recursoDto = modelMapper.map(recurso, RecursoDTO.class);
        return recursoDto;
    }

    // Guardar recurso
    public RecursoDTO guardar(RecursoDTO recursoDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Recurso recurso = modelMapper.map(recursoDTO, Recurso.class);
        recurso = recursoRepository.save(recurso);
        recursoDTO = modelMapper.map(recurso, RecursoDTO.class);
        return recursoDTO;
    }

}
