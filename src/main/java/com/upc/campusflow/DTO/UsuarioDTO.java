package com.upc.campusflow.DTO;

import com.upc.campusflow.Model.Estudiante;
import com.upc.campusflow.Model.Profesor;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String username;
    private String password;
    private Long id_Estudiante;
    private Long id_Profesor;
    private boolean Estado;
}
