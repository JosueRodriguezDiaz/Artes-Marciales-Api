package com.daw.artesmarciales.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO de salida estándar enviado al cliente en las operaciones de lectura y escritura.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de salida con la información completa de un Arte Marcial")
public class ArteMarcialResumenDTO {

    @Schema(description = "ID único del registro", example = "1")
    private Long id;

    @Schema(description = "Código oficial de federación", example = "AM-JP-001")
    private String codigoFederacion;

    @Schema(description = "Nombre del arte marcial", example = "Karate Shotokan")
    private String nombre;

    @Schema(description = "País de origen", example = "Japon")
    private String paisOrigen;

    @Schema(description = "¿Es disciplina olímpica?", example = "true")
    private Boolean disciplinaOlimpica;

    @Schema(description = "Cuota mensual en euros", example = "55.50")
    private Double cuotaMensual;

    @Schema(description = "Años de antigüedad", example = "102")
    private Integer aniosHistoria;

    @Schema(description = "Email de contacto", example = "contacto@karateshotokan.jp")
    private String emailFederacion;

    @Schema(description = "Fecha de fundación", example = "1922-05-01")
    private LocalDate fechaFundacion;

    @Schema(description = "Técnicas principales", example = "[\"Mae Geri\", \"Gyaku Zuki\"]")
    private List<String> tecnicasPrincipales;
}
