package com.upc.campusflow.Service;
import com.upc.campusflow.DTO.TareaDTO;
import com.upc.campusflow.Model.Tarea;
import com.upc.campusflow.Model.Estudiante;
import com.upc.campusflow.Model.Horario;
import com.upc.campusflow.Repository.AsignaturaRepository;
import com.upc.campusflow.Repository.TareaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TareaService {
    final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository, AsignaturaRepository asignaturaRepository) {
        this.tareaRepository = tareaRepository;
    }
    //RELACION CON ESTUDIANTE Y HORARIO
    //listar
    public List<TareaDTO> listar() {
        List<Tarea> tareas = tareaRepository.findAll()
                .stream()
                .filter(Tarea::isEstado) //filtrar solo los activos
                .toList();
        List<TareaDTO> tareaDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Tarea tarea : tareas) {
            TareaDTO tareaDTO = modelMapper.map(tarea, TareaDTO.class);
            if(tarea.getEstudiante()!=null && tarea.getHorario()!=null) {
                tareaDTO.setId_estudiante(tarea.getEstudiante().getIdEstudiante());
                tareaDTO.setId_horario(tarea.getHorario().getIdHorario());
            }
            tareaDTOS.add(tareaDTO);
        }
        return tareaDTOS;
    }

    //guardar
    public TareaDTO guardar(TareaDTO tareaDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Tarea tarea = modelMapper.map(tareaDTO, Tarea.class);
        if(tarea.getEstudiante()!=null && tarea.getHorario()!=null) {
            Estudiante estudiante = new Estudiante();
            Horario horario = new Horario();
            estudiante.setIdEstudiante(tareaDTO.getId_estudiante());
            horario.setIdHorario(tareaDTO.getId_horario());
            tarea.setHorario(horario);
            tarea.setEstudiante(estudiante);
        }
        tarea=tareaRepository.save(tarea);
        tareaDTO=modelMapper.map(tarea, TareaDTO.class);
        return tareaDTO;
    }


    // Modificar
    public TareaDTO modificar(Long id, TareaDTO tareaDTO) {
        ModelMapper modelMapper = new ModelMapper();
        // Verificar si la tarea existe
        Tarea existente= tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada con ID: " + id));
        modelMapper.map(tareaDTO, existente); // sobreescribir los datos

        if (tareaDTO.getId_estudiante()!=null && tareaDTO.getId_horario()!=null) {
            Estudiante estudiante = new Estudiante();
            Horario horario = new Horario();
            estudiante.setIdEstudiante(tareaDTO.getId_estudiante());
            horario.setIdHorario(tareaDTO.getId_horario());
            existente.setHorario(horario);
            existente.setEstudiante(estudiante);
        }

        Tarea actualizar=tareaRepository.save(existente);
        TareaDTO dto=modelMapper.map(actualizar, TareaDTO.class);
        return dto;  // Si no se encuentra la tarea, devolvemos null
    }

    //Eliminar
    public TareaDTO eliminar(Long id) {
        ModelMapper modelMapper = new ModelMapper();
        Tarea entidad= tareaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Tarea no encontrada con ID: " + id));
        entidad.setEstado(false);
        entidad=tareaRepository.save(entidad);
        TareaDTO dto=modelMapper.map(entidad, TareaDTO.class);
        return dto;
    }

    //Obtener tareas activas por estudiante
    public List<TareaDTO> TareasActivasPorEstudiante(Long idEstudiante) {
        List<Tarea> tareas = tareaRepository.TareasActivasPorEstudiante(idEstudiante);

        List<TareaDTO> tareaDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Tarea tarea : tareas) {
            TareaDTO tareaDTO = modelMapper.map(tarea, TareaDTO.class);
            if (tarea.getEstudiante() != null && tarea.getHorario() != null) {
                tareaDTO.setId_estudiante(tarea.getEstudiante().getIdEstudiante());
                tareaDTO.setId_horario(tarea.getHorario().getIdHorario());
            }
            tareaDTOS.add(tareaDTO);
        }
        return tareaDTOS;
    }
    //Obtener tareas con una prioridad especifica y ordenarlas por fecha limite
    public List<TareaDTO> TareasPorPrioridad(String prioridad) {
        List<Tarea> tareas = tareaRepository.TareasPorPrioridad(prioridad);

        List<TareaDTO> tareaDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Tarea tarea : tareas) {
            TareaDTO tareaDTO = modelMapper.map(tarea, TareaDTO.class);
            if (tarea.getEstudiante() != null && tarea.getHorario() != null) {
                tareaDTO.setId_estudiante(tarea.getEstudiante().getIdEstudiante());
                tareaDTO.setId_horario(tarea.getHorario().getIdHorario());
            }
            tareaDTOS.add(tareaDTO);
        }
        return tareaDTOS;
    }
}
