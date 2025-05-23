package com.upc.campusflow.Service;
import com.upc.campusflow.DTO.PublicacionDTO;
import com.upc.campusflow.Model.GrupoForo;
import com.upc.campusflow.Model.Publicacion;
import com.upc.campusflow.Repository.PublicacionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<String, Long> resumenPorLabel() {
        return publicacionRepository
                .countByLabel()
                .stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long)   arr[1]
                ));
    }

    /**
     * Lista publicaciones de un grupo de foro con un label específico
     */
    public List<PublicacionDTO> listarPorGrupoYLabel(Long idGrupoForo, String label) {
        List<Publicacion> publicaciones = publicacionRepository.findByGrupoForoAndLabel(idGrupoForo, label);
        List<PublicacionDTO> dtos = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        for (Publicacion p : publicaciones) {
            PublicacionDTO dto = mapper.map(p, PublicacionDTO.class);
            dto.setIdGrupoForo(p.getGrupoForo());
            dtos.add(dto);
        }
        return dtos;
    }


    /**
     * Lista publicaciones filtradas por grupo de foro y fecha exacta
     */
    public List<PublicacionDTO> listarPorGrupoYFecha(Long idGrupoForo, LocalDate fecha) {
        List<Publicacion> publicaciones = publicacionRepository.findByGrupoForoAndFecha(idGrupoForo, fecha);
        return mapToDTO(publicaciones);
    }

    private List<PublicacionDTO> mapToDTO(List<Publicacion> publicaciones) {
        ModelMapper mapper = new ModelMapper();
        List<PublicacionDTO> dtos = new ArrayList<>();
        for (Publicacion p : publicaciones) {
            PublicacionDTO dto = mapper.map(p, PublicacionDTO.class);
            dto.setIdGrupoForo(p.getGrupoForo());
            dtos.add(dto);
        }
        return dtos;
    }
}
