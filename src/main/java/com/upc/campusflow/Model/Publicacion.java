package com.upc.campusflow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String Contenido;
    private LocalDate Fecha;
    private String label;
    private boolean Estado;

    @ManyToOne
    @JoinColumn(name = "IdGrupoF")
    private GrupoForo idGrupoForo;


}
