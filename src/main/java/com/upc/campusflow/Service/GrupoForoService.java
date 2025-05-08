package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.GrupoForoDTO;
import com.upc.campusflow.Model.Asignatura;
import com.upc.campusflow.Model.GrupoForo;
import com.upc.campusflow.Model.Nota;
import com.upc.campusflow.Repository.GrupoForoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        for (GrupoForo grupoForo : grupoForos) {
            GrupoForoDTO grupoForoDTO = modelMapper.map(grupoForo, GrupoForoDTO.class);
            if(grupoForo.getAsignatura() != null){
                grupoForoDTO.setId_Asigneatura(grupoForo.getAsignatura().getIdAsignatura());
            }
            grupoForoDTOS.add(grupoForoDTO);
        }
        return grupoForoDTOS;
    }

    //Guardar
    public GrupoForoDTO guardar(GrupoForoDTO grupoForoDTO){
        ModelMapper modelMapper = new ModelMapper();
        GrupoForo grupoForo = modelMapper.map(grupoForoDTO, GrupoForo.class);
        if(grupoForoDTO.getId_Asigneatura() != null){
            Asignatura asignatura = new Asignatura();
            asignatura.setIdAsignatura(grupoForoDTO.getId_Asigneatura());
            grupoForo.setAsignatura(asignatura);
        }
        grupoForo = grupoForoRepository.save(grupoForo);
        grupoForoDTO = modelMapper.map(grupoForo, GrupoForoDTO.class);
        return grupoForoDTO;

    }

    //Modificar
    public GrupoForoDTO modificar (Long id, GrupoForoDTO grupoForoDTO){
        ModelMapper modelMapper = new ModelMapper();
        GrupoForo exits = grupoForoRepository.findById(id).orElseThrow(() -> new RuntimeException("Foro no encontrado con ID: " + id));
        modelMapper.map(grupoForoDTO, exits);

        if(grupoForoDTO.getId_Asigneatura() != null){
            Asignatura asignatura = new Asignatura();
            asignatura.setIdAsignatura(grupoForoDTO.getId_Asigneatura());
            exits.setAsignatura(asignatura);
        }

        GrupoForo actualizado = grupoForoRepository.save(exits);
        GrupoForoDTO dto =  modelMapper.map(actualizado, GrupoForoDTO.class);
        return dto;
    }

    //Eliminar
    public GrupoForoDTO eliminar (Long id){
        ModelMapper modelMapper = new ModelMapper();
        GrupoForo entidad = grupoForoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foro no encontrado con ID: " + id));
        entidad.setEstado(false);
        entidad =  grupoForoRepository.save(entidad);
        GrupoForoDTO dto =  modelMapper.map(entidad, GrupoForoDTO.class);
        return dto;
    }

}