package com.upc.campusflow.Service;
import com.upc.campusflow.DTO.PublicacionDTO;
import com.upc.campusflow.Model.GrupoForo;
import com.upc.campusflow.Model.Publicacion;
import com.upc.campusflow.Repository.PublicacionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublicacionService {

    final PublicacionRepository publicacionRepository;

    public PublicacionService(PublicacionRepository publicacionRepository) {
        this.publicacionRepository = publicacionRepository;
    }

    public PublicacionDTO guardar(PublicacionDTO publicacionDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Publicacion publicacion = modelMapper.map(publicacionDTO, Publicacion.class);

        if (publicacionDTO.getIdGrupoForo() != null) {
            GrupoForo grupoForo = new GrupoForo();
            grupoForo.setId(publicacionDTO.getId());
            publicacion.setIdGrupoForo(grupoForo);
        }

        publicacion = publicacionRepository.save(publicacion);
        return modelMapper.map(publicacion, PublicacionDTO.class);
    }

    public PublicacionDTO buscarPorId(Long id) {
        Publicacion publicacion = publicacionRepository.findById(id).orElse(null);
        if (publicacion == null) return null;

        ModelMapper modelMapper = new ModelMapper();
        PublicacionDTO dto = modelMapper.map(publicacion, PublicacionDTO.class);

        if (publicacion.getIdGrupoForo() != null) {
            dto.setIdGrupoForo(publicacion.getIdGrupoForo());
        }

        return dto;
    }

    public List<PublicacionDTO> listar() {
        List<Publicacion> publicaciones = publicacionRepository.findAll();
        List<PublicacionDTO> listaDTO = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        for (Publicacion publicacion : publicaciones) {
            PublicacionDTO dto = modelMapper.map(publicacion, PublicacionDTO.class);
            listaDTO.add(dto);
        }

        return listaDTO;
    }
}
