package com.daw.artesmarciales.controller;

import com.daw.artesmarciales.model.dto.*;
import com.daw.artesmarciales.service.ArteMarcialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Capa de Controlador REST (MVC).
 */
@RestController
@RequestMapping("/api/artes-marciales")
@RequiredArgsConstructor
@Tag(name = "Artes Marciales", description = "Gestión integral CRUD y Mashup de disciplinas de Artes Marciales")
public class ArteMarcialController {

    private final ArteMarcialService service;

    @GetMapping
    @Operation(summary = "Listar todas las artes marciales",
            description = "Obtiene el catálogo completo. Permite filtrar opcionalmente por nombre parcial o país de origen.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public ResponseEntity<List<ArteMarcialResumenDTO>> listarTodas(
            @Parameter(description = "Filtrar por coincidencia en el nombre (ej. Karate)")
            @RequestParam(required = false) String nombre,
            @Parameter(description = "Filtrar por país de origen exacto (ej. Japon)")
            @RequestParam(required = false) String pais) {

        List<ArteMarcialResumenDTO> lista = service.listarTodos(nombre, pais);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un arte marcial por ID",
            description = "Busca y devuelve los datos de una disciplina concreta a partir de su ID numérico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Arte marcial encontrado"),
            @ApiResponse(responseCode = "404", description = "Arte marcial no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ArteMarcialResumenDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/{id}/mashup")
    @Operation(summary = "Obtener ficha híbrida (Mashup con API Externa + Caché RAM)",
            description = "Combina los datos locales del arte marcial con información geográfica en tiempo real de su país de origen (REST Countries API). Resultado cacheado en RAM.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ficha mashup generada correctamente"),
            @ApiResponse(responseCode = "404", description = "Arte marcial no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ArteMarcialMashupDTO> obtenerMashup(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerMashupPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo arte marcial",
            description = "Crea una nueva disciplina tras validar todas las reglas de formato y negocio.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Arte marcial creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto: código o nombre duplicado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Violación de regla de negocio",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ArteMarcialResumenDTO> crear(@Valid @RequestBody ArteMarcialCreacionDTO dto) {
        ArteMarcialResumenDTO creado = service.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Reemplazar completamente un arte marcial (PUT)",
            description = "Actualiza todos los atributos de un registro existente. Requiere enviar el objeto completo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Arte marcial actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Arte marcial no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Violación de regla de negocio",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ArteMarcialResumenDTO> actualizarCompleto(
            @PathVariable Long id,
            @Valid @RequestBody ArteMarcialCreacionDTO dto) {
        return ResponseEntity.ok(service.actualizarCompleto(id, dto));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un arte marcial (PATCH)",
            description = "Modifica únicamente los campos enviados en el JSON sin alterar el resto de atributos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualización parcial aplicada"),
            @ApiResponse(responseCode = "400", description = "Formato inválido en algún campo enviado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Arte marcial no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Violación de regla de negocio",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ArteMarcialResumenDTO> actualizarParcial(
            @PathVariable Long id,
            @Valid @RequestBody ArteMarcialPatchDTO dto) {
        return ResponseEntity.ok(service.actualizarParcial(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un arte marcial",
            description = "Elimina permanentemente el registro de la base de datos y limpia la caché.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Arte marcial eliminado correctamente (sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Arte marcial no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
