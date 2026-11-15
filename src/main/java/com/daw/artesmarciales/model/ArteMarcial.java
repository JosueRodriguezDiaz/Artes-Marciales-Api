package com.daw.artesmarciales.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa un Arte Marcial en la base de datos relacional.
 */
@Entity
@Table(name = "artes_marciales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad de base de datos que representa un Arte Marcial registrado")
public class ArteMarcial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único autoincremental en base de datos", example = "1")
    private Long id;

    @Column(name = "codigo_federacion", nullable = false, unique = true, length = 15)
    @Schema(description = "Código federativo único del arte marcial", example = "AM-JP-001")
    private String codigoFederacion;

    @Column(name = "nombre", nullable = false, unique = true, length = 80)
    @Schema(description = "Nombre oficial del estilo o arte marcial", example = "Karate Shotokan")
    private String nombre;

    @Column(name = "pais_origen", nullable = false, length = 60)
    @Schema(description = "País histórico de origen (en español)", example = "Japon")
    private String paisOrigen;

    @Column(name = "disciplina_olimpica", nullable = false)
    @Schema(description = "Indica si forma parte del programa oficial de los Juegos Olímpicos", example = "true")
    private Boolean disciplinaOlimpica;

    @Column(name = "cuota_mensual", nullable = false)
    @Schema(description = "Cuota mensual media de entrenamiento en euros", example = "55.50")
    private Double cuotaMensual;

    @Column(name = "anios_historia", nullable = false)
    @Schema(description = "Antigüedad estimada en años", example = "102")
    private Integer aniosHistoria;

    @Column(name = "email_federacion", nullable = false, length = 100)
    @Schema(description = "Correo electrónico oficial de contacto", example = "contacto@karateshotokan.jp")
    private String emailFederacion;

    @Column(name = "fecha_fundacion", nullable = false)
    @Schema(description = "Fecha oficial de fundación o sistematización moderna", example = "1922-05-01")
    private LocalDate fechaFundacion;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "arte_marcial_tecnicas", joinColumns = @JoinColumn(name = "arte_marcial_id"))
    @Column(name = "tecnica", nullable = false, length = 60)
    @Builder.Default
    @Schema(description = "Listado de técnicas o katas representativos", example = "[\"Mae Geri\", \"Gyaku Zuki\"]")
    private List<String> tecnicasPrincipales = new ArrayList<>();
}
