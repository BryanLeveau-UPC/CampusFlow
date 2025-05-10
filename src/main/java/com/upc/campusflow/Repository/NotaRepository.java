package com.upc.campusflow.Repository;

import com.upc.campusflow.Model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotaRepository extends JpaRepository<Nota, Long> {
    // Consulta para las notas de un determinado Curso
    @Query("SELECT n FROM Nota n WHERE n.asignatura.idAsignatura = :idAsignatura AND n.Estado = true")
    List<Nota> findNotasByAsignaturaId(Long idAsignatura);
}
