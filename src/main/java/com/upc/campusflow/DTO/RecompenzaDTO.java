package com.upc.campusflow.DTO;

import com.upc.campusflow.Model.EstudianteEstadistica;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecompenzaDTO {
    private Long IDRecompenza;
    private String Plataforma;
    private String URL;
    private Long id_EstudianteEstadistica;
    private boolean Estado;
}
