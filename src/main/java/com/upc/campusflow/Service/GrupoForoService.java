package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.GrupoForoDTO;
import com.upc.campusflow.Model.Asignatura;
import com.upc.campusflow.Model.GrupoForo;
import com.upc.campusflow.Repository.GrupoForoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GrupoForoService {

    final GrupoForoRepository grupoForoRepository;

    public GrupoForoService(GrupoForoRepository grupoForoRepository) {
        this.grupoForoRepository = grupoForoRepository;
    }

    //Listar
    public List<GrupoForoDTO> listar(){

        List<GrupoForo> grupoForos = grupoForoRepository.findAll().stream().filter(GrupoForo::isEstado).toList();
        List<GrupoForoDTO> grupoForoDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for(GrupoForo grupoForo : grupoForos){
            GrupoForoDTO grupoForoDTO = modelMapper.map(grupoForo, GrupoForoDTO.class);
            if(grupoForoDTO.getId_asignatura() != null){
                grupoForoDTO.setId_asignatura(grupoForo.getAsignatura().getIdAsignatura());
            }
            grupoForoDTOS.add(grupoForoDTO);
        }
        return grupoForoDTOS;
    }

    //Guardar
    public GrupoForoDTO guardar(GrupoForoDTO grupoForoDTO){
        ModelMapper modelMapper = new ModelMapper();
        GrupoForo grupoForo = modelMapper.map(grupoForoDTO, GrupoForo.class);
        if(grupoForoDTO.getId_asignatura()!= null){
            Asignatura asignatura = new Asignatura();
            asignatura.setIdAsignatura(grupoForoDTO.getId_asignatura());
            grupoForo.setAsignatura(asignatura);
        }
        grupoForo = grupoForoRepository.save(grupoForo);
        grupoForoDTO = modelMapper.map(grupoForo, GrupoForoDTO.class);
        return grupoForoDTO;

    }

    //Modificar
    public GrupoForoDTO modificar (Long id, GrupoForoDTO grupoForoDTO){
        ModelMapper modelMapper = new ModelMapper();
        GrupoForo exits = (GrupoForo) grupoForoRepository.findById(id).orElseThrow(() -> new RuntimeException("Foro no encontrado con ID: " + id));
        modelMapper.map(grupoForoDTO, exits);

        if(grupoForoDTO.getId_asignatura() != null){
            Asignatura asignatura = new Asignatura();
            asignatura.setIdAsignatura(grupoForoDTO.getId_asignatura());
            exits.setAsignatura(asignatura);
        }

        GrupoForo actualize = grupoForoRepository.save(exits);
        GrupoForoDTO dto =  modelMapper.map(actualize, GrupoForoDTO.class);
        return dto;
    }

    //Eliminar
    public GrupoForoDTO eliminar (Long id){
        ModelMapper modelMapper = new ModelMapper();
        GrupoForo entidad = (GrupoForo) grupoForoRepository.findById(id).orElseThrow(() -> new RuntimeException("Foro no encontrado con ID: " + id));
        entidad.setEstado(false);
        entidad =  grupoForoRepository.save(entidad);
        GrupoForoDTO dto =  modelMapper.map(entidad, GrupoForoDTO.class);
        return dto;
    }

}