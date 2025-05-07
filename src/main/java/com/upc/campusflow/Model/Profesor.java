package com.upc.campusflow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfesor;
    private String especialidad;
    private String numColegiatura;
    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario Usuario;
    private boolean Estado;

}
