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
            grupoForo.setIdGrupoForo(publicacionDTO.getIdGrupoForo().getIdGrupoForo());
            publicacion.setGrupoForo(grupoForo);
        }

        publicacion = publicacionRepository.save(publicacion);
        return modelMapper.map(publicacion, PublicacionDTO.class);
    }

    public PublicacionDTO buscarPorId(Long id) {
        Publicacion publicacion = publicacionRepository.findById(id).orElse(null);
        if (publicacion == null) return null;

        ModelMapper modelMapper = new ModelMapper();
        PublicacionDTO dto = modelMapper.map(publicacion, PublicacionDTO.class);

        if (publicacion.getGrupoForo() != null) {
            dto.setIdGrupoForo(publicacion.getGrupoForo());
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

    public PublicacionDTO modificar(Long id, PublicacionDTO publicacionDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Publicacion existente = publicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con ID: " + id));
        modelMapper.map(publicacionDTO, existente); // Sobrescribe los datos existentes

        Publicacion actualizado = publicacionRepository.save(existente);
        return modelMapper.map(actualizado, PublicacionDTO.class);
    }

    public PublicacionDTO eliminar(Long id) {
        Publicacion publicacion = publicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con ID: " + id));

        publicacion.setEstado(false);
        publicacion = publicacionRepository.save(publicacion);
        ModelMapper modelMapper = new ModelMapper();
        PublicacionDTO dto = modelMapper.map(publicacion, PublicacionDTO.class);

        return dto;
    }
}
