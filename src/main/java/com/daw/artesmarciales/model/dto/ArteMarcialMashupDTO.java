package com.daw.artesmarciales.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * DTO Híbrido (Mashup) que combina datos locales de H2 con datos de la API externa REST Countries.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO Híbrido (Mashup) que fusiona los datos locales del Arte Marcial con datos geográficos de una API externa")
public class ArteMarcialMashupDTO {

    @Schema(description = "ID del arte marcial en BD local", example = "1")
    private Long id;

    @Schema(description = "Código federativo", example = "AM-JP-001")
    private String codigoFederacion;

    @Schema(description = "Nombre del arte marcial", example = "Karate Shotokan")
    private String nombre;

    @Schema(description = "País de origen registrado localmente", example = "Japon")
    private String paisOrigen;

    @Schema(description = "Cuota mensual en euros", example = "55.50")
    private Double cuotaMensual;

    @Schema(description = "Técnicas principales", example = "[\"Mae Geri\", \"Gyaku Zuki\"]")
    private List<String> tecnicasPrincipales;

    @Schema(description = "Capital oficial del país obtenida de REST Countries API", example = "Tokyo")
    private String capitalPais;

    @Schema(description = "Continente o región geográfica obtenida de API externa", example = "Asia")
    private String regionGeografica;

    @Schema(description = "Población total del país de origen según API externa", example = "125836021")
    private Long poblacionPais;

    @Schema(description = "URL de la bandera oficial del país", example = "https://flagcdn.com/w320/jp.png")
    private String banderaUrl;

    @Schema(description = "Indica si los datos externos provienen de la API en vivo o del modo Fallback", example = "API Externa REST Countries (En vivo)")
    private String origenDatosExternos;
}
