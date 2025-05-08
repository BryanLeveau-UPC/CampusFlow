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

public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTarea;
    private String titulo;
    private String descripcion;
    private Date fechaLimite;
    private String prioridad;
    private boolean estado = true;
}
