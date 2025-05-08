package com.upc.campusflow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecursoDTO {
    private int idRecurso;
    private String tipoArchivo;
    private String url;
    private Date fechaSubida;
    private boolean Estado;
}
