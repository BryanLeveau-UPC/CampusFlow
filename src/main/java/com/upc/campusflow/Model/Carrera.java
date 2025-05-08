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
public class Carrera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarrera;
    private int nombre;
    private byte[] mallaCurricular;

    @OneToMany(mappedBy = "carrera", cascade  = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Estudiante> estudiantes;
    @OneToMany(mappedBy = "carrera", cascade  = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Asignatura> asignaturas;

    private boolean Estado = true;

    public static boolean isEstado(Object o) {

        return false;
    }
}