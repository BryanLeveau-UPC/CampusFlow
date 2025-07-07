package com.upc.campusflow.Repository;

import com.upc.campusflow.Model.Recompensa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecompensaRepository extends JpaRepository<Recompensa, Long> {
    // Buscar recompensas activas por id del estudiante

}
