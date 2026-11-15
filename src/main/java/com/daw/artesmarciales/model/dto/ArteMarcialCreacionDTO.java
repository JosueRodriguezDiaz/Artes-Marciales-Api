package com.daw.artesmarciales.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO de entrada para operaciones de creación (POST) y reemplazo completo (PUT).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO requerido para crear o reemplazar completamente un Arte Marcial")
public class ArteMarcialCreacionDTO {

    @NotBlank(message = "El código de federación es obligatorio y no puede estar en blanco")
    @Pattern(regexp = "^AM-[A-Z]{2}-\\d{3}$", message = "El código debe seguir el formato AM-XX-000 (ej. AM-JP-001)")
    @Schema(description = "Código federativo con formato AM-XX-000", example = "AM-TH-005")
    private String codigoFederacion;

    @NotBlank(message = "El nombre del arte marcial es obligatorio")
    @Size(min = 3, max = 80, message = "El nombre debe tener entre 3 y 80 caracteres")
    @Schema(description = "Nombre del arte marcial", example = "Muay Thai")
    private String nombre;

    @NotBlank(message = "El país de origen es obligatorio")
    @Size(min = 3, max = 60, message = "El país de origen debe tener entre 3 y 60 caracteres")
    @Schema(description = "País de origen", example = "Tailandia")
    private String paisOrigen;

    @NotNull(message = "Debe especificar si es disciplina olímpica (true/false)")
    @Schema(description = "Indica si es deporte olímpico", example = "false")
    private Boolean disciplinaOlimpica;

    @NotNull(message = "La cuota mensual es obligatoria")
    @Positive(message = "La cuota mensual debe ser un importe estrictamente positivo")
    @Digits(integer = 4, fraction = 2, message = "La cuota admite máximo 4 enteros y 2 decimales")
    @Schema(description = "Cuota mensual en euros", example = "65.00")
    private Double cuotaMensual;

    @NotNull(message = "Los años de historia son obligatorios")
    @Min(value = 1, message = "Debe tener al menos 1 año de historia")
    @Schema(description = "Antigüedad en años", example = "300")
    private Integer aniosHistoria;

    @NotBlank(message = "El email de la federación es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    @Schema(description = "Correo de contacto oficial", example = "info@muaythai.th")
    private String emailFederacion;

    @NotNull(message = "La fecha de fundación es obligatoria")
    @PastOrPresent(message = "La fecha de fundación no puede ser posterior a hoy")
    @Schema(description = "Fecha de creación o fundación", example = "1767-04-07")
    private LocalDate fechaFundacion;

    @NotEmpty(message = "Debe incluir al menos una técnica principal")
    @Schema(description = "Lista de técnicas características", example = "[\"Teep\", \"Sok Ti\", \"Khao Loi\"]")
    private List<String> tecnicasPrincipales;

    @AssertTrue(message = "Debe confirmar que el arte marcial cumple la normativa de seguridad y federación")
    @Schema(description = "Confirmación obligatoria de cumplimiento normativo", example = "true")
    private Boolean normativaVerificada;
}
