package com.upc.campusflow.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotaDTO {
    private Long idNota;
    private String Tipo;
    private double Puntaje;
    private double Peso_Nota;
    private Long id_asignatura;
    private boolean Estado;

}
