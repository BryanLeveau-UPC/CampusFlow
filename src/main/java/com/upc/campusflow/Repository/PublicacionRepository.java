package com.upc.campusflow.Repository;

import com.upc.campusflow.Model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
}