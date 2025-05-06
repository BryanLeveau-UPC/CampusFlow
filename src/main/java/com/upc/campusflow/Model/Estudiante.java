package com.upc.campusflow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long IdEstudiante;

    @OneToOne
    @JoinColumn(name = "id_estudianteEstadistica")
    private EstudianteEstadistica estudianteEstadistica;
}
