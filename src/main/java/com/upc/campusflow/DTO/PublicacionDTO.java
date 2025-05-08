package com.upc.campusflow.DTO;

import com.upc.campusflow.Model.GrupoForo;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicacionDTO {
    private Long Id;
    private String Contenido;
    private LocalDate Fecha;
    private String label;
    private GrupoForo idGrupoForo;
    private boolean Estado;
}
