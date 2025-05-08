package com.upc.campusflow.Repository;


import com.upc.campusflow.Model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}
