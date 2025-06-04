package com.upc.campusflow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteEstadistica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idEstudianteEstadistica;

    //tipo de variable cambiar LocalDate
    private Date TotalHoraEstudiante;

    private int TareasCompletadas;

    //localDate
    private Date UltimaConexion;

    //Ver
    @OneToOne
    @JoinColumn(name = "id_estudiante")
    private Estudiante estudiante;

    @OneToMany(mappedBy = "estudianteEstadistica", cascade  = CascadeType.ALL, fetch = FetchType.LAZY)
    private List  <Recompensa> recompensas;

    private boolean Estado = true;
}