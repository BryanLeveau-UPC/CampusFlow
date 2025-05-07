package com.upc.campusflow.DTO;

import com.upc.campusflow.Model.Usuario;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfesorDTO {

    private Long idProfesor;
    private String especialidad;
    private String numColegiatura;
    private Long id_Usuario;
    private boolean Estado;

}
