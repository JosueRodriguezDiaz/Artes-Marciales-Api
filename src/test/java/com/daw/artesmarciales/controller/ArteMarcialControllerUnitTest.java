package com.daw.artesmarciales.controller;

import com.daw.artesmarciales.exception.RecursoNoEncontradoException;
import com.daw.artesmarciales.model.dto.ArteMarcialResumenDTO;
import com.daw.artesmarciales.service.ArteMarcialService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArteMarcialController.class)
class ArteMarcialControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArteMarcialService service;

    @Test
    @DisplayName("GET /api/artes-marciales debe devolver 200 OK y la lista en JSON")
    void testListarTodas() throws Exception {
        ArteMarcialResumenDTO dto = ArteMarcialResumenDTO.builder()
                .id(1L)
                .codigoFederacion("AM-JP-001")
                .nombre("Karate Shotokan")
                .paisOrigen("Japon")
                .disciplinaOlimpica(true)
                .cuotaMensual(55.50)
                .aniosHistoria(102)
                .emailFederacion("info@karate.jp")
                .fechaFundacion(LocalDate.of(1922, 5, 1))
                .tecnicasPrincipales(List.of("Mae Geri"))
                .build();

        when(service.listarTodos(null, null)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/artes-marciales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Karate Shotokan"))
                .andExpect(jsonPath("$[0].codigoFederacion").value("AM-JP-001"));

        verify(service, times(1)).listarTodos(null, null);
    }

    @Test
    @DisplayName("GET /api/artes-marciales/999 debe devolver 404 Not Found gestionado por RestControllerAdvice")
    void testObtenerPorIdNoEncontrado() throws Exception {
        when(service.obtenerPorId(999L))
                .thenThrow(new RecursoNoEncontradoException("No existe el ID 999"));

        mockMvc.perform(get("/api/artes-marciales/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensaje").value("No existe el ID 999"));
    }

    @Test
    @DisplayName("DELETE /api/artes-marciales/1 debe devolver 204 No Content con cuerpo vacío")
    void testEliminar() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/artes-marciales/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(service, times(1)).eliminar(1L);
    }
}
