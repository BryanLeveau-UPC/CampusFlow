package com.upc.campusflow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private int NiveldePrioridad;
    @OneToOne
    @JoinColumn(name = "id_Estudiante")
    private Estudiante Estudiante;
    private boolean Estado = true;
}
