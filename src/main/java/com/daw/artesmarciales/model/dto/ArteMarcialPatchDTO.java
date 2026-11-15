package com.daw.artesmarciales.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO de entrada para actualizaciones parciales (PATCH).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para actualización parcial (PATCH): solo se modifican los campos enviados (no nulos)")
public class ArteMarcialPatchDTO {

    @Pattern(regexp = "^AM-[A-Z]{2}-\\d{3}$", message = "El código debe seguir el formato AM-XX-000")
    @Schema(description = "Nuevo código federativo (opcional)", example = "AM-JP-099")
    private String codigoFederacion;

    @Size(min = 3, max = 80, message = "El nombre debe tener entre 3 y 80 caracteres")
    @Schema(description = "Nuevo nombre (opcional)", example = "Karate Kyokushin")
    private String nombre;

    @Size(min = 3, max = 60, message = "El país debe tener entre 3 y 60 caracteres")
    @Schema(description = "Nuevo país de origen (opcional)", example = "Japon")
    private String paisOrigen;

    @Schema(description = "Actualizar estado olímpico (opcional)", example = "false")
    private Boolean disciplinaOlimpica;

    @Positive(message = "La cuota mensual debe ser positiva")
    @Digits(integer = 4, fraction = 2, message = "Máximo 4 enteros y 2 decimales")
    @Schema(description = "Nueva cuota mensual (opcional)", example = "62.50")
    private Double cuotaMensual;

    @Min(value = 1, message = "Debe tener al menos 1 año de historia")
    @Schema(description = "Nuevos años de historia (opcional)", example = "60")
    private Integer aniosHistoria;

    @Email(message = "El formato del correo electrónico no es válido")
    @Schema(description = "Nuevo email de contacto (opcional)", example = "nuevo@kyokushin.jp")
    private String emailFederacion;

    @PastOrPresent(message = "La fecha de fundación no puede ser futura")
    @Schema(description = "Nueva fecha de fundación (opcional)", example = "1964-06-01")
    private LocalDate fechaFundacion;

    @Schema(description = "Nueva lista de técnicas (opcional)", example = "[\"Mawashi Geri\", \"Seiken Tsuki\"]")
    private List<String> tecnicasPrincipales;
}
