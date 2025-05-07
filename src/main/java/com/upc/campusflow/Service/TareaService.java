package com.upc.campusflow.Service;
import com.upc.campusflow.DTO.TareaDTO;
import com.upc.campusflow.Model.Tarea;
import com.upc.campusflow.Repository.TareaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TareaService {
    final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    //Método para listar las tareas
    public List<TareaDTO> listar() {
        List<Tarea> tareas = tareaRepository.findAll();
        List<TareaDTO> tareasDTO = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Tarea tarea : tareas) {
            TareaDTO tareaDTO = modelMapper.map(tarea, TareaDTO.class);
            tareasDTO.add(tareaDTO);
        }
        return tareasDTO;
    }

    // Método para buscar tarea por ID
    public TareaDTO buscarPorId(int idTarea) {
        Tarea tarea = tareaRepository.findById(idTarea).get();
        ModelMapper modelMapper = new ModelMapper();
        TareaDTO tareaDTO = modelMapper.map(tarea, TareaDTO.class);
        return tareaDTO;
    }

    // Método para guardar una nueva tarea
    public TareaDTO guardar(TareaDTO tareaDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Tarea tarea = modelMapper.map(tareaDTO, Tarea.class);
        tarea = tareaRepository.save(tarea);
        tareaDTO= modelMapper.map(tareaDTO, TareaDTO.class);
        return tareaDTO;
    }

    // Método para actualizar una tarea existente
    public TareaDTO actualizar(int idTarea, TareaDTO tareaDTO) {
        ModelMapper modelMapper = new ModelMapper();
        // Verificar si la tarea existe
        if (tareaRepository.existsById(idTarea)) {
            // Mapear el DTO a la entidad
            Tarea tarea = modelMapper.map(tareaDTO, Tarea.class);
            tarea.setIdTarea(idTarea);  // Aseguramos que el ID sea el correcto para la actualización

            // Guardar la tarea actualizada
            tarea = tareaRepository.save(tarea);

            // Mapear la entidad actualizada de vuelta al DTO
            tareaDTO = modelMapper.map(tarea, TareaDTO.class);
            return tareaDTO;  // Devolver el DTO actualizado
        }
        return null;  // Si no se encuentra la tarea, devolvemos null
    }

    // Método para eliminar una tarea
    public boolean eliminar(int idTarea) {
        if (tareaRepository.existsById(idTarea)) {
            tareaRepository.deleteById(idTarea);  // Eliminar la tarea por ID
            return true;  // Devolver true si la tarea fue eliminada correctamente
        }
        return false;  // Si la tarea no existe, devolver false
    }



}
