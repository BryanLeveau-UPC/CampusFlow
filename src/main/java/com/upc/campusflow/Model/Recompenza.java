package com.upc.campusflow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recompenza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IDRecompenza;
    private String Plataforma;
    private String URL;
    @ManyToOne
    @JoinColumn(name = "id_EstudianteEstadistica")
    private EstudianteEstadistica EstudianteEstadistica;
    private boolean Estado = true;
}
