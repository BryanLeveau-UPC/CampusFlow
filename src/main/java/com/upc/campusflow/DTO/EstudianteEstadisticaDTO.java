package com.upc.campusflow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteEstadisticaDTO {
    private Long idEstudianteEstadistica;
    private Date TotalHoraEstudiante;
    private int TareasCompletadas;
    private Date UltimaConexion;
    private int NiveldePrioridad;
    private Long Id_Estudiante;
    private boolean Estado;

}
