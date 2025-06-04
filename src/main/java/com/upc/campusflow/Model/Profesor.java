package com.upc.campusflow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfesor;
    private String especialidad;
    private String numColegiatura;
    private boolean Estado = true;

    @OneToMany(mappedBy = "profesor")
    private List<Evento> eventos;

    //profesor es un usuario
    @OneToOne(mappedBy = "profesor")
    private Usuario usuarios;



}
