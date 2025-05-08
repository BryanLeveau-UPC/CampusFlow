package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.RecompensaDTO;
import com.upc.campusflow.Model.EstudianteEstadistica;
import com.upc.campusflow.Model.Recompensa;
import com.upc.campusflow.Repository.RecompensaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecompensaService {
    final RecompensaRepository recompensaRepository;

    public RecompensaService(RecompensaRepository recompensaRepository) {
        this.recompensaRepository = recompensaRepository;
    }
    //Listar
    public List<RecompensaDTO> Listar() {
        List<Recompensa> recompensas = recompensaRepository.findAll().stream()
                .filter(Recompensa::isEstado) // Filtrar solo los activos
                .toList();
        List<RecompensaDTO> recompensaDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Recompensa recompensa : recompensas) {
            RecompensaDTO recompensaDTO = modelMapper.map(recompensa, RecompensaDTO.class);
            if (recompensa.getEstudianteEstadistica() != null) {
                recompensaDTO.setId_EstudianteEstadistica(recompensa.getEstudianteEstadistica().getIdEstudianteEstadistica());
            }
            recompensaDTOS.add(recompensaDTO);
        }
        return recompensaDTOS;
    }

    //Guardar
    public RecompensaDTO guardar(RecompensaDTO recompensaDTO){
        ModelMapper modelMapper = new ModelMapper();
        Recompensa recompensa = modelMapper.map(recompensaDTO, Recompensa.class);
        if(recompensaDTO.getId_EstudianteEstadistica() != null){
            EstudianteEstadistica estudianteEstadistica = new EstudianteEstadistica();
            estudianteEstadistica.setIdEstudianteEstadistica(recompensaDTO.getId_EstudianteEstadistica());
            recompensa.setEstudianteEstadistica(estudianteEstadistica);
        }
        recompensa = recompensaRepository.save(recompensa);
        recompensaDTO = modelMapper.map(recompensa, RecompensaDTO.class);
        return recompensaDTO;
    }
    //Modificar
    public RecompensaDTO modificar(Long id, RecompensaDTO recompensaDTO) {
        Recompensa existente = recompensaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recopmenza no encontrada con ID: " + id));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(recompensaDTO, existente); // Sobrescribe los datos existentes

        if (recompensaDTO.getId_EstudianteEstadistica() != null) {
            EstudianteEstadistica estudianteestadistica = new EstudianteEstadistica();
            estudianteestadistica.setIdEstudianteEstadistica(recompensaDTO.getId_EstudianteEstadistica());
            existente.setEstudianteEstadistica(estudianteestadistica);
        }

        Recompensa actualizado = recompensaRepository.save(existente);
        RecompensaDTO dto = modelMapper.map(actualizado, RecompensaDTO.class);
        return dto;
    }

    //Eliminar lógico
    public RecompensaDTO eliminar(Long id) {
        ModelMapper modelMapper = new ModelMapper();
        Recompensa entidad = recompensaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recompenza no encontrada con ID: " + id));
        entidad.setEstado(false);
        entidad =  recompensaRepository.save(entidad);
        RecompensaDTO dto = modelMapper.map(entidad, RecompensaDTO.class);
        return  dto;
    }
}
