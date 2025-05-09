package com.upc.campusflow.Repository;

import com.upc.campusflow.Model.Publicacion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    @Query("""
      SELECT p.label AS label, COUNT(p) AS total
      FROM Publicacion p
      GROUP BY p.label
      """)
    List<Object[]> countByLabel();
}