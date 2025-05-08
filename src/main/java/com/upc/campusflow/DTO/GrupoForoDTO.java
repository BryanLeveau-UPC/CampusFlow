package com.upc.campusflow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupoForoDTO {
    private Long idGrupoForo;
    private String titulo;
    private String descripcion;
    private String campo;
    private Date fechaCreacion;
    private Long id_asignatura;
    private boolean Estado;

}
