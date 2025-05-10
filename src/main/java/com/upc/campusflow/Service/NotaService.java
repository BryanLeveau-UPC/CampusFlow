package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.EstudianteEstadisticaDTO;
import com.upc.campusflow.DTO.NotaDTO;
import com.upc.campusflow.Model.Asignatura;
import com.upc.campusflow.Model.Estudiante;
import com.upc.campusflow.Model.EstudianteEstadistica;
import com.upc.campusflow.Model.Nota;
import com.upc.campusflow.Repository.NotaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotaService {
    final NotaRepository notaRepository;

    public NotaService(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }
    //listar
    public List<NotaDTO> Listar(){
        List<Nota> notas = notaRepository.findAll()
                .stream()
                .filter(Nota::isEstado) // Filtrar solo los activos
                .toList();
        List<NotaDTO> notaDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Nota nota : notas) {
            NotaDTO notaDTO = modelMapper.map(nota, NotaDTO.class);
            if(nota.getAsignatura() != null){
                notaDTO.setId_asignatura(nota.getAsignatura().getIdAsignatura());
            }
            notaDTOS.add(notaDTO);
        }
        return notaDTOS;
    }
    //guardar

    public NotaDTO guardar(NotaDTO notaDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Nota nota = modelMapper.map(notaDTO, Nota.class);

        // Aquí es donde debes setear la Asignatura manualmente
        if (notaDTO.getId_asignatura() != null) {
            Asignatura asignatura = new Asignatura();
            asignatura.setIdAsignatura(notaDTO.getId_asignatura());
            nota.setAsignatura(asignatura);
        }

        nota = notaRepository.save(nota);
        notaDTO = modelMapper.map(nota, NotaDTO.class);
        notaDTO.setId_asignatura(nota.getAsignatura().getIdAsignatura());
        return notaDTO;
    }
    //Modificar
    public NotaDTO modificar(Long id, NotaDTO notaDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Nota existente = notaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con ID: " + id));
        modelMapper.map(notaDTO, existente); // Sobrescribe los datos existentes

        if (notaDTO.getId_asignatura() != null) {
            Asignatura asignatura = new Asignatura();
            asignatura.setIdAsignatura(notaDTO.getId_asignatura());
            existente.setAsignatura(asignatura);
        }

        Nota actualizado = notaRepository.save(existente);
        NotaDTO dto = modelMapper.map(actualizado, NotaDTO.class);
        return dto;
    }

    //Eliminar lógico
    public NotaDTO eliminar(Long id) {
        ModelMapper modelMapper = new ModelMapper();
        Nota entidad = notaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con ID: " + id));
        entidad.setEstado(false);
        entidad =  notaRepository.save(entidad);
        NotaDTO dto = modelMapper.map(entidad, NotaDTO.class);
        return  dto;
    }

    //Listar notas por asignatura de alumno
    public List<NotaDTO> obtenerNotasPorAsignatura(Long idAsignatura) {
        List<Nota> notas = notaRepository.findNotasByAsignaturaId(idAsignatura);
        List<NotaDTO> notaDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Nota nota : notas) {
            NotaDTO notaDTO = modelMapper.map(nota, NotaDTO.class);
            notaDTO.setId_asignatura(nota.getAsignatura().getIdAsignatura());
            notaDTOS.add(notaDTO);
        }
        return notaDTOS;
    }

}
