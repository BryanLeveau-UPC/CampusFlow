package com.upc.campusflow.Repository;

import com.upc.campusflow.Model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Estudiante,Long> {
}
