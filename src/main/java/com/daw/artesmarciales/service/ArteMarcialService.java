package com.daw.artesmarciales.service;

import com.daw.artesmarciales.exception.RecursoNoEncontradoException;
import com.daw.artesmarciales.exception.ReglaNegocioException;
import com.daw.artesmarciales.model.ArteMarcial;
import com.daw.artesmarciales.model.dto.*;
import com.daw.artesmarciales.repository.ArteMarcialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Capa de Lógica de Negocio (Service).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ArteMarcialService {

    private final ArteMarcialRepository repository;
    private final ExternalApiService externalApiService;

    @Transactional(readOnly = true)
    public List<ArteMarcialResumenDTO> listarTodos(String nombre, String pais) {
        List<ArteMarcial> resultados;

        if (nombre != null && !nombre.isBlank()) {
            resultados = repository.findByNombreContainingIgnoreCase(nombre);
        } else if (pais != null && !pais.isBlank()) {
            resultados = repository.findByPaisOrigenIgnoreCase(pais);
        } else {
            resultados = repository.findAll();
        }

        return resultados.stream()
                .map(this::mapearAResumenDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ArteMarcialResumenDTO obtenerPorId(Long id) {
        ArteMarcial entidad = buscarEntidadPorId(id);
        return mapearAResumenDTO(entidad);
    }

    @Cacheable(value = "mashupArtesMarciales", key = "#id")
    @Transactional(readOnly = true)
    public ArteMarcialMashupDTO obtenerMashupPorId(Long id) {
        log.info(">>> [CACHE MISS] Calculando Mashup desde BD + API Externa para ID: {}", id);
        ArteMarcial entidad = buscarEntidadPorId(id);

        ExternalApiService.DatosPaisExterno datosPais = externalApiService.obtenerDatosPais(entidad.getPaisOrigen());

        return ArteMarcialMashupDTO.builder()
                .id(entidad.getId())
                .codigoFederacion(entidad.getCodigoFederacion())
                .nombre(entidad.getNombre())
                .paisOrigen(entidad.getPaisOrigen())
                .cuotaMensual(entidad.getCuotaMensual())
                .tecnicasPrincipales(entidad.getTecnicasPrincipales())
                .capitalPais(datosPais.capital())
                .regionGeografica(datosPais.region())
                .poblacionPais(datosPais.poblacion())
                .banderaUrl(datosPais.banderaUrl())
                .origenDatosExternos(datosPais.origen())
                .build();
    }

    @CacheEvict(value = "mashupArtesMarciales", allEntries = true)
    @Transactional
    public ArteMarcialResumenDTO crear(ArteMarcialCreacionDTO dto) {
        validarReglasNegocio(dto.getDisciplinaOlimpica(), dto.getCuotaMensual(), dto.getAniosHistoria());

        ArteMarcial nuevaEntidad = ArteMarcial.builder()
                .codigoFederacion(dto.getCodigoFederacion())
                .nombre(dto.getNombre())
                .paisOrigen(dto.getPaisOrigen())
                .disciplinaOlimpica(dto.getDisciplinaOlimpica())
                .cuotaMensual(dto.getCuotaMensual())
                .aniosHistoria(dto.getAniosHistoria())
                .emailFederacion(dto.getEmailFederacion())
                .fechaFundacion(dto.getFechaFundacion())
                .tecnicasPrincipales(new ArrayList<>(dto.getTecnicasPrincipales()))
                .build();

        ArteMarcial guardada = repository.save(nuevaEntidad);
        return mapearAResumenDTO(guardada);
    }

    @CacheEvict(value = "mashupArtesMarciales", allEntries = true)
    @Transactional
    public ArteMarcialResumenDTO actualizarCompleto(Long id, ArteMarcialCreacionDTO dto) {
        ArteMarcial existente = buscarEntidadPorId(id);
        validarReglasNegocio(dto.getDisciplinaOlimpica(), dto.getCuotaMensual(), dto.getAniosHistoria());

        existente.setCodigoFederacion(dto.getCodigoFederacion());
        existente.setNombre(dto.getNombre());
        existente.setPaisOrigen(dto.getPaisOrigen());
        existente.setDisciplinaOlimpica(dto.getDisciplinaOlimpica());
        existente.setCuotaMensual(dto.getCuotaMensual());
        existente.setAniosHistoria(dto.getAniosHistoria());
        existente.setEmailFederacion(dto.getEmailFederacion());
        existente.setFechaFundacion(dto.getFechaFundacion());
        existente.setTecnicasPrincipales(new ArrayList<>(dto.getTecnicasPrincipales()));

        ArteMarcial actualizada = repository.save(existente);
        return mapearAResumenDTO(actualizada);
    }

    @CacheEvict(value = "mashupArtesMarciales", allEntries = true)
    @Transactional
    public ArteMarcialResumenDTO actualizarParcial(Long id, ArteMarcialPatchDTO dto) {
        ArteMarcial existente = buscarEntidadPorId(id);

        if (dto.getCodigoFederacion() != null) existente.setCodigoFederacion(dto.getCodigoFederacion());
        if (dto.getNombre() != null) existente.setNombre(dto.getNombre());
        if (dto.getPaisOrigen() != null) existente.setPaisOrigen(dto.getPaisOrigen());
        if (dto.getDisciplinaOlimpica() != null) existente.setDisciplinaOlimpica(dto.getDisciplinaOlimpica());
        if (dto.getCuotaMensual() != null) existente.setCuotaMensual(dto.getCuotaMensual());
        if (dto.getAniosHistoria() != null) existente.setAniosHistoria(dto.getAniosHistoria());
        if (dto.getEmailFederacion() != null) existente.setEmailFederacion(dto.getEmailFederacion());
        if (dto.getFechaFundacion() != null) existente.setFechaFundacion(dto.getFechaFundacion());
        if (dto.getTecnicasPrincipales() != null) {
            existente.setTecnicasPrincipales(new ArrayList<>(dto.getTecnicasPrincipales()));
        }

        validarReglasNegocio(existente.getDisciplinaOlimpica(), existente.getCuotaMensual(), existente.getAniosHistoria());

        ArteMarcial actualizada = repository.save(existente);
        return mapearAResumenDTO(actualizada);
    }

    @CacheEvict(value = "mashupArtesMarciales", allEntries = true)
    @Transactional
    public void eliminar(Long id) {
        ArteMarcial existente = buscarEntidadPorId(id);
        repository.delete(existente);
    }

    private ArteMarcial buscarEntidadPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se ha encontrado ningún arte marcial con el ID: " + id));
    }

    private void validarReglasNegocio(Boolean esOlimpica, Double cuotaMensual, Integer aniosHistoria) {
        if (Boolean.TRUE.equals(esOlimpica) && cuotaMensual != null && cuotaMensual < 20.0) {
            throw new ReglaNegocioException(
                    "Regla de negocio violada: Una disciplina olímpica requiere una cuota mínima de 20.00 € para cubrir el seguro federativo.");
        }
        if (aniosHistoria != null && aniosHistoria > 2500) {
            throw new ReglaNegocioException(
                    "Regla de negocio violada: La antigüedad documentada no puede superar los 2500 años.");
        }
    }

    private ArteMarcialResumenDTO mapearAResumenDTO(ArteMarcial entidad) {
        return ArteMarcialResumenDTO.builder()
                .id(entidad.getId())
                .codigoFederacion(entidad.getCodigoFederacion())
                .nombre(entidad.getNombre())
                .paisOrigen(entidad.getPaisOrigen())
                .disciplinaOlimpica(entidad.getDisciplinaOlimpica())
                .cuotaMensual(entidad.getCuotaMensual())
                .aniosHistoria(entidad.getAniosHistoria())
                .emailFederacion(entidad.getEmailFederacion())
                .fechaFundacion(entidad.getFechaFundacion())
                .tecnicasPrincipales(entidad.getTecnicasPrincipales())
                .build();
    }
}
