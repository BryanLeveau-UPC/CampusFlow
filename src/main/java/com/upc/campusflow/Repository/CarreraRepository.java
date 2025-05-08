package com.upc.campusflow.Repository;

import com.upc.campusflow.Model.Carrera;
import com.upc.campusflow.Model.GrupoForo;
import com.upc.campusflow.Model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarreraRepository extends JpaRepository<Carrera, Long> {
}
