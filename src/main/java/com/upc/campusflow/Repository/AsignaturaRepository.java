package com.upc.campusflow.Repository;

import com.upc.campusflow.Model.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {
    // Consulta para obtener las 3 asignaturas con mayor promedio
    @Query("SELECT a FROM Asignatura a " +
            "JOIN a.notas n " +
            "GROUP BY a.idAsignatura " +
            "ORDER BY AVG(n.Puntaje) DESC")
    List<Asignatura> obtenerTop3AsignaturasPorPromedio();
}
