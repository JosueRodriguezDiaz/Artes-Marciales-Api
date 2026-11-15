package com.daw.artesmarciales.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO unificado para todas las respuestas de error de la API (HTTP 400, 404, 409, 422, 500).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Estructura estándar de respuesta ante cualquier error en la API")
public class ErrorResponse {

    @Schema(description = "Código de estado HTTP del error", example = "400")
    private int status;

    @Schema(description = "Mensaje descriptivo general del error", example = "Error de validación en los datos enviados")
    private String mensaje;

    @Schema(description = "Fecha y hora exacta en la que ocurrió el error", example = "2026-11-15T15:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Mapa con el detalle de los campos inválidos y el motivo (solo en errores de validación)",
            example = "{\"cuotaMensual\": \"La cuota mensual debe ser un importe estrictamente positivo\"}")
    private Map<String, String> erroresCampo;
}
