package com.upc.campusflow.Repository;

import com.upc.campusflow.Model.Evento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    /**
     * Consulta: eventos por profesor
     */
    @Query("SELECT e FROM Evento e WHERE e.profesor.idProfesor = :idProfesor")
    List<Evento> findEventosByProfesorId(@Param("idProfesor") Long idProfesor);

    /**
     * Próximos 5 eventos para un estudiante,
     * usando la propiedad exacta de la entidad Estudiante (IdEstudiante).
     */
    @Query(
            "SELECT ev " +
                    "FROM Evento ev JOIN ev.estudiantes e " +
                    "WHERE e.IdEstudiante = :idEstudiante " +
                    "AND ev.FechaInicio >= :fechaActual " +
                    "ORDER BY ev.FechaInicio ASC"
    )
    List<Evento> findNext5EventosByEstudiante(
            @Param("idEstudiante") Long idEstudiante,
            @Param("fechaActual") LocalDate fechaActual,
            Pageable pageable
    );
}