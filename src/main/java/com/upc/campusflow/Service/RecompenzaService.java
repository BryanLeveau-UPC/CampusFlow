package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.EstudianteEstadisticaDTO;
import com.upc.campusflow.DTO.RecompenzaDTO;
import com.upc.campusflow.Model.Estudiante;
import com.upc.campusflow.Model.EstudianteEstadistica;
import com.upc.campusflow.Model.Recompenza;
import com.upc.campusflow.Repository.RecompenzaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecompenzaService {
    final RecompenzaRepository recompenzaRepository;

    public RecompenzaService(RecompenzaRepository recompenzaRepository) {
        this.recompenzaRepository = recompenzaRepository;
    }
    //Listar
    public List<RecompenzaDTO> Listar() {
        List<Recompenza> recompenzas = recompenzaRepository.findAll().stream()
                .filter(Recompenza::isEstado) // Filtrar solo los activos
                .toList();
        List<RecompenzaDTO> recompenzaDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Recompenza recompenza : recompenzas) {
            RecompenzaDTO recompenzaDTO = modelMapper.map(recompenza, RecompenzaDTO.class);
            if (recompenza.getEstudianteEstadistica() != null) {
                recompenzaDTO.setId_EstudianteEstadistica(recompenza.getEstudianteEstadistica().getIdEstudianteEstadistica());
            }
            recompenzaDTOS.add(recompenzaDTO);
        }
        return recompenzaDTOS;
    }

    //Guardar
    public RecompenzaDTO guardar(RecompenzaDTO recompenzaDTO){
        ModelMapper modelMapper = new ModelMapper();
        Recompenza recompenza = modelMapper.map(recompenzaDTO, Recompenza.class);
        if(recompenzaDTO.getId_EstudianteEstadistica() != null){
            EstudianteEstadistica estudianteEstadistica = new EstudianteEstadistica();
            estudianteEstadistica.setIdEstudianteEstadistica(recompenzaDTO.getId_EstudianteEstadistica());
            recompenza.setEstudianteEstadistica(estudianteEstadistica);
        }
        recompenza = recompenzaRepository.save(recompenza);
        recompenzaDTO = modelMapper.map(recompenza, RecompenzaDTO.class);
        return recompenzaDTO;
    }
    //Modificar
    public RecompenzaDTO modificar(Long id, RecompenzaDTO recompenzaDTO) {
        Recompenza existente = recompenzaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recopmenza no encontrada con ID: " + id));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(recompenzaDTO, existente); // Sobrescribe los datos existentes

        if (recompenzaDTO.getId_EstudianteEstadistica() != null) {
            EstudianteEstadistica estudianteestadistica = new EstudianteEstadistica();
            estudianteestadistica.setIdEstudianteEstadistica(recompenzaDTO.getId_EstudianteEstadistica());
            existente.setEstudianteEstadistica(estudianteestadistica);
        }

        Recompenza actualizado = recompenzaRepository.save(existente);
        RecompenzaDTO dto = modelMapper.map(actualizado, RecompenzaDTO.class);
        return dto;
    }

    //Eliminar lógico
    public RecompenzaDTO eliminar(Long id) {
        ModelMapper modelMapper = new ModelMapper();
        Recompenza entidad = recompenzaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recompenza no encontrada con ID: " + id));
        entidad.setEstado(false);
        entidad =  recompenzaRepository.save(entidad);
        RecompenzaDTO dto = modelMapper.map(entidad, RecompenzaDTO.class);
        return  dto;
    }
}
