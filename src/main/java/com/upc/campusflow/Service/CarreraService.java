package com.upc.campusflow.Service;


import com.upc.campusflow.DTO.CarreraDTO;
import com.upc.campusflow.Model.Carrera;
import com.upc.campusflow.Repository.CarreraRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarreraService {
    final CarreraRepository carreraRepository;

    public CarreraService(CarreraRepository carreraRepository) {
        this.carreraRepository = carreraRepository;
    }

    //Listar
    public List<CarreraDTO> listar(){
        List<Object> list = new ArrayList<>();
        for (Object o : carreraRepository.findAll()) {
            if (Carrera.isEstado(o)) {
                list.add(o);
            }
        }
        List<Object> carreras = list;
        List<CarreraDTO> carreraDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        for (Object carrera : carreras) {
            CarreraDTO carreraDTO = modelMapper.map(carrera, CarreraDTO.class);
            carreraDTOS.add(carreraDTO);
        }
        return carreraDTOS;
    }

    //Guardar
    public CarreraDTO guardar(CarreraDTO carreraDTO){
        ModelMapper modelMapper = new ModelMapper();
        Carrera carrera = modelMapper.map(carreraDTO, Carrera.class);
        carrera = carreraRepository.save(carrera);
        carreraDTO = modelMapper.map(carrera, CarreraDTO.class);
        return carreraDTO;
    }

    //Modificar

    public CarreraDTO modificar (Long id , CarreraDTO carreraDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Carrera exist = null;
        carreraRepository.findAll().clear();
        modelMapper.map(carreraDTO, exist);

        Carrera actualize = carreraRepository.save(exist);
        CarreraDTO dto = modelMapper.map(actualize, CarreraDTO.class);
        return dto;

    }

    //Eliminar
    public CarreraDTO eliminar (Long id) {
        ModelMapper modelMapper = new ModelMapper();
        Carrera entidad = (Carrera) carreraRepository.findById(id).orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: " + id));
        entidad.setEstado(false);
        entidad = carreraRepository.save(entidad);
        CarreraDTO dto = modelMapper.map(entidad, CarreraDTO.class);
        return dto;
    }
}