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
    private Date TotalHoraEstudiante;
    private int TareasCompletadas;
    private Date UltimaConexion;
    private int NiveldeProductividad;
    @OneToOne
    @JoinColumn(name = "id_estudiante")
    private Estudiante Estudiante;
    @OneToMany(mappedBy = "estudianteEstadistica", cascade  = CascadeType.ALL, fetch = FetchType.LAZY)
    private List  <Recompensa> recompensas;
    private boolean Estado = true;
}