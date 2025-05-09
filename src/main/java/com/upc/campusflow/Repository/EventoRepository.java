package com.upc.campusflow.Repository;


import com.upc.campusflow.Model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    @Query("SELECT e FROM Evento e WHERE e.profesor.idProfesor = :idProfesor")
    List<Evento> findEventosByProfesorId(@Param("idProfesor") Long idProfesor);
}
