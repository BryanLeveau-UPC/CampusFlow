package com.upc.campusflow.Repository;

import com.upc.campusflow.Model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EstudianteRepository extends JpaRepository<Estudiante,Long> {
    //Estudiantes con al menos una nota menor a 11
    @Query("SELECT DISTINCT e FROM Estudiante e JOIN e.notas n " +
            "WHERE n.Puntaje < 11")
    List<Estudiante> findEstudiantesConNotaMenorA11();

}
