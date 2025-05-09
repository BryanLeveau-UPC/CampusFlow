package com.upc.campusflow.Repository;

import com.upc.campusflow.Model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {


    /**   estudiantes activos por ciclo
     */
    @Query("""
      SELECT e.ciclo AS ciclo, COUNT(e) AS total
      FROM Estudiante e
      WHERE e.estado = true
      GROUP BY e.ciclo
      """)
    List<Object[]> countActiveByCiclo();
}