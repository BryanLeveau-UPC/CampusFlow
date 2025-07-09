package com.upc.campusflow.Repository;

import com.upc.campusflow.Model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    /**
     * Conteo de publicaciones agrupadas por label
     */
    @Query(
            "SELECT p.label AS label, COUNT(p) AS total " +
                    "FROM Publicacion p " +
                    "GROUP BY p.label"
    )
    List<Object[]> countByLabel();

    /**
     * Filtra publicaciones por grupo de foro y label exacto
     */
    @Query(
            "SELECT p " +
                    "FROM Publicacion p " +
                    "WHERE p.grupoForo.idGrupoForo = :idGrupoForo " +
                    "AND p.label = :label"
    )
    List<Publicacion> findByGrupoForoAndLabel(
            @Param("idGrupoForo") Long idGrupoForo,
            @Param("label")       String label
    );


    /**
     * Filtra publicaciones por grupo de foro y fecha exacta
     */
    @Query("SELECT p FROM Publicacion p WHERE p.grupoForo.idGrupoForo = :idGrupoForo AND p.fecha = :fecha")
    List<Publicacion> findByGrupoForoIdGrupoForoAndFecha(Long idGrupoForo, LocalDate fecha);


    List<Publicacion> findByGrupoForo_IdGrupoForo(Long idGrupoForo);
}


