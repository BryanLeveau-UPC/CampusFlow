package com.upc.campusflow.Repository;

import com.upc.campusflow.Model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    List<Rol> findByNombre(String nombre);
}
