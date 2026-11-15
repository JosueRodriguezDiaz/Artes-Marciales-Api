package com.daw.artesmarciales.service;

import com.daw.artesmarciales.exception.RecursoNoEncontradoException;
import com.daw.artesmarciales.exception.ReglaNegocioException;
import com.daw.artesmarciales.model.ArteMarcial;
import com.daw.artesmarciales.model.dto.ArteMarcialCreacionDTO;
import com.daw.artesmarciales.model.dto.ArteMarcialMashupDTO;
import com.daw.artesmarciales.model.dto.ArteMarcialResumenDTO;
import com.daw.artesmarciales.repository.ArteMarcialRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArteMarcialServiceUnitTest {

    @Mock
    private ArteMarcialRepository repository;

    @Mock
    private ExternalApiService externalApiService;

    @InjectMocks
    private ArteMarcialService service;

    @Test
    @DisplayName("Debe crear un arte marcial correctamente cuando cumple todas las reglas")
    void testCrearExito() {
        ArteMarcialCreacionDTO dto = ArteMarcialCreacionDTO.builder()
                .codigoFederacion("AM-JP-020")
                .nombre("Kendo")
                .paisOrigen("Japon")
                .disciplinaOlimpica(false)
                .cuotaMensual(50.0)
                .aniosHistoria(150)
                .emailFederacion("kendo@jp.org")
                .fechaFundacion(LocalDate.of(1895, 1, 1))
                .tecnicasPrincipales(List.of("Men", "Kote", "Do"))
                .normativaVerificada(true)
                .build();

        ArteMarcial entidadGuardada = ArteMarcial.builder()
                .id(1L)
                .codigoFederacion(dto.getCodigoFederacion())
                .nombre(dto.getNombre())
                .paisOrigen(dto.getPaisOrigen())
                .disciplinaOlimpica(dto.getDisciplinaOlimpica())
                .cuotaMensual(dto.getCuotaMensual())
                .aniosHistoria(dto.getAniosHistoria())
                .emailFederacion(dto.getEmailFederacion())
                .fechaFundacion(dto.getFechaFundacion())
                .tecnicasPrincipales(dto.getTecnicasPrincipales())
                .build();

        when(repository.save(any(ArteMarcial.class))).thenReturn(entidadGuardada);

        ArteMarcialResumenDTO resultado = service.crear(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Kendo");
        verify(repository, times(1)).save(any(ArteMarcial.class));
    }

    @Test
    @DisplayName("Debe lanzar ReglaNegocioException si una disciplina olímpica tiene cuota menor a 20€")
    void testCrearReglaNegocioInvalida() {
        ArteMarcialCreacionDTO dtoInvalido = ArteMarcialCreacionDTO.builder()
                .codigoFederacion("AM-JP-021")
                .nombre("Lucha Libre Olímpica")
                .paisOrigen("Grecia")
                .disciplinaOlimpica(true)
                .cuotaMensual(15.0)
                .aniosHistoria(500)
                .emailFederacion("lucha@olimpica.gr")
                .fechaFundacion(LocalDate.of(1896, 4, 6))
                .tecnicasPrincipales(List.of("Suplex"))
                .normativaVerificada(true)
                .build();

        assertThatThrownBy(() -> service.crear(dtoInvalido))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("cuota mínima de 20.00");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException cuando el ID no existe")
    void testObtenerPorIdNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Debe construir el DTO híbrido (Mashup) fusionando BD y API externa")
    void testObtenerMashupPorId() {
        ArteMarcial entidad = ArteMarcial.builder()
                .id(1L)
                .codigoFederacion("AM-JP-001")
                .nombre("Karate")
                .paisOrigen("Japon")
                .cuotaMensual(55.0)
                .tecnicasPrincipales(List.of("Mae Geri"))
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(entidad));
        when(externalApiService.obtenerDatosPais("Japon")).thenReturn(
                new ExternalApiService.DatosPaisExterno("Tokyo", "Asia", 125000000L, "url_bandera", "API Externa")
        );

        ArteMarcialMashupDTO mashup = service.obtenerMashupPorId(1L);

        assertThat(mashup.getNombre()).isEqualTo("Karate");
        assertThat(mashup.getCapitalPais()).isEqualTo("Tokyo");
        assertThat(mashup.getRegionGeografica()).isEqualTo("Asia");
    }
}
