package com.upc.campusflow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TareaDTO {
    private int idTarea;
    private String titulo;
    private String descripcion;
    private Date fechaLimite;
    private String prioridad;
    private boolean estado;
}
