package com.upc.campusflow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteEstadisticaDTO {
    private Long idEstudianteEstadistica;
    private LocalDate TotalHoraEstudiante;
    private int TareasCompletadas;




    private LocalDate UltimaConexion;
    private int NiveldePrioridad;
    private Long Id_Estudiante;
    private boolean Estado;

}