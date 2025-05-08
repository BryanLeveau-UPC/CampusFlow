package com.upc.campusflow.DTO;

import com.upc.campusflow.Model.Asignatura;
import com.upc.campusflow.Model.Publicacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupoForoDTO {
    private long idGrupoForo;
    private String titulo;
    private String descripcion;
    private String campo;
    private Date fechaCreacion;
    private Long id_asignatura;
    private boolean Estado = true;
}