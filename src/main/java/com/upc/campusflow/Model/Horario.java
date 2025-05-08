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
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idHorario;
    private Date dia;
    private Date horaInicio;
    private Date horaFin;

    @OneToMany(mappedBy = "horario", cascade  = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Asignatura> asignaturas;
    private boolean Estado;
}
