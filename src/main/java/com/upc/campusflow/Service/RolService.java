package com.upc.campusflow.Service;

import com.upc.campusflow.DTO.UsuarioDTO;
import com.upc.campusflow.Model.Rol;
import com.upc.campusflow.Model.Usuario;
import com.upc.campusflow.Repository.RolRepository;
import com.upc.campusflow.Repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {
    final RolRepository rolRepository;
    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    //Guardar
    public Rol guardarRol(Rol rol) {
        return rolRepository.save(rol);
    }


    public Rol buscarRolPorNombre(String nombre) {
        List<Rol> roles = rolRepository.findByNombre(nombre);
        return roles.isEmpty() ? null : roles.get(0); // Devuelve el primero si existe
    }

    // Crear rol si no existe
    public Rol obtenerOCrearRol(String nombre) {
        List<Rol> roles = rolRepository.findByNombre(nombre);
        if (roles.isEmpty()) {
            Rol nuevoRol = new Rol();
            nuevoRol.setNombre(nombre);
            return rolRepository.save(nuevoRol);
        }
        return roles.get(0);
    }

}
