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
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long IdEstudiante;

    private int Ciclo;

    //lista de nota
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Nota> notas;

    @ManyToOne
    @JoinColumn(name = "idCarrera")
    private Carrera carrera;

    //
    @OneToOne(mappedBy = "estudiante")
    private Usuario usuarios;

    @ManyToMany(mappedBy = "estudiantes")
    private List<Evento> eventos;


    private boolean Estado = true;

}
