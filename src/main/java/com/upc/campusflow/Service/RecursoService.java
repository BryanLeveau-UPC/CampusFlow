package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.AsignaturaDTO;
import com.upc.campusflow.DTO.RecursoDTO;
import com.upc.campusflow.Model.Asignatura;
import com.upc.campusflow.Model.Publicacion;
import com.upc.campusflow.Model.Recurso;
import com.upc.campusflow.Model.Tarea;
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
    //RELACION CON TAREA Y PUBLICACION
    // Listar todos los recursos
    public List<RecursoDTO> listar() {
        List<Recurso> recursos = recursoRepository.findAll()
                .stream()
                .filter(Recurso::isEstado)
                .toList();
        List<RecursoDTO> recursoDTOs = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Recurso recurso : recursos) {
            RecursoDTO recursoDTO = modelMapper.map(recurso, RecursoDTO.class);
            if(recurso.getTarea()!=null && recurso.getPublicacion()!=null){
                recursoDTO.setId_tarea(recurso.getTarea().getIdTarea());
                recursoDTO.setId_publicacion(recurso.getPublicacion().getIdPublicacion());
            }
            recursoDTOs.add(recursoDTO);
        }
        return recursoDTOs;
    }

    // Guardar recurso
    public RecursoDTO guardar(RecursoDTO recursoDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Recurso recurso = modelMapper.map(recursoDTO, Recurso.class);
        if(recurso.getTarea()!=null && recurso.getPublicacion()!=null){
            Tarea tarea = new Tarea();
            Publicacion publicacion = new Publicacion();
            tarea.setIdTarea(recursoDTO.getId_tarea());
            publicacion.setIdPublicacion(recursoDTO.getId_publicacion());
            recurso.setPublicacion(publicacion);
            recurso.setTarea(tarea);
        }
        recurso = recursoRepository.save(recurso);
        recursoDTO = modelMapper.map(recurso, RecursoDTO.class);
        return recursoDTO;
    }

    //Modificar
    public RecursoDTO modificar(Long id, RecursoDTO recursoDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Recurso existe= recursoRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Recurso no encontrado con ID: " + id));
        modelMapper.map(existe, recursoDTO);

        if(recursoDTO.getId_tarea()!=null && recursoDTO.getId_publicacion()!=null){
            Tarea tarea = new Tarea();
            Publicacion publicacion = new Publicacion();
            tarea.setIdTarea(recursoDTO.getId_tarea());
            publicacion.setIdPublicacion(recursoDTO.getId_publicacion());
            existe.setTarea(tarea);
            existe.setPublicacion(publicacion);
        }
        Recurso actualizar= recursoRepository.save(existe);
        RecursoDTO dto= modelMapper.map(actualizar, RecursoDTO.class);
        return dto;
    }

    //Eliminar lógico
    public RecursoDTO eliminar(Long id) {
        ModelMapper modelMapper = new ModelMapper();
        Recurso entidad = recursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrada con ID: " + id));
        entidad.setEstado(false);
        entidad =  recursoRepository.save(entidad);
        RecursoDTO dto = modelMapper.map(entidad, RecursoDTO.class);
        return  dto;
    }

}
