package com.upc.campusflow.DTO;

import com.upc.campusflow.Model.Asignatura;
import com.upc.campusflow.Model.Estudiante;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor



@NoArgsConstructor
public class CarreraDTO {

    private Long idCarrera;
    private int nombre;
    private byte[] mallaCurricular;
    private boolean Estado = true;


}