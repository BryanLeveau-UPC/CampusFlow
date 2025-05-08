package com.upc.campusflow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    @OneToOne
    @JoinColumn(name = "id_estudiante")
    private Estudiante Estudiante;
    @OneToOne
    @JoinColumn(name = "id_profesor")
    private Profesor Profesor;
    private boolean Estado = true;

}
