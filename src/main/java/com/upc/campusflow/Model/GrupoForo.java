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
public class GrupoForo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idGrupoForo;
    private String titulo;
    private String descripcion;
    private String campo;
    private Date fechaCreacion;
    @OneToOne
    @JoinColumn(name = "id_asignatura")
    private Asignatura Asignatura;
    @OneToMany(mappedBy = "grupoForo", cascade  = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Publicacion> publicaciones;
    private boolean Estado = true;

    public static boolean isEstado(Object t) {

        return false;
    }
}