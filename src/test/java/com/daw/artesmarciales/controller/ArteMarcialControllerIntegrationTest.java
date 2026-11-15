package com.daw.artesmarciales.controller;

import com.daw.artesmarciales.model.dto.ArteMarcialCreacionDTO;
import com.daw.artesmarciales.model.dto.ArteMarcialPatchDTO;
import com.daw.artesmarciales.repository.ArteMarcialRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ArteMarcialControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArteMarcialRepository repository;

    @BeforeEach
    void limpiarBaseDatos() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Flujo completo CRUD: POST (201) -> GET (200) -> PATCH (200) -> DELETE (204) -> GET (404)")
    void testFlujoCompletoCrud() throws Exception {
        ArteMarcialCreacionDTO nuevo = ArteMarcialCreacionDTO.builder()
                .codigoFederacion("AM-TH-050")
                .nombre("Muay Thai Boran")
                .paisOrigen("Tailandia")
                .disciplinaOlimpica(false)
                .cuotaMensual(65.00)
                .aniosHistoria(250)
                .emailFederacion("info@muaythai.th")
                .fechaFundacion(LocalDate.of(1774, 3, 17))
                .tecnicasPrincipales(List.of("Teep", "Sok Ti"))
                .normativaVerificada(true)
                .build();

        String respuestaPost = mockMvc.perform(post("/api/artes-marciales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Muay Thai Boran"))
                .andReturn().getResponse().getContentAsString();

        Long idCreado = objectMapper.readTree(respuestaPost).get("id").asLong();

        mockMvc.perform(get("/api/artes-marciales/" + idCreado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoFederacion").value("AM-TH-050"));

        ArteMarcialPatchDTO patch = ArteMarcialPatchDTO.builder()
                .cuotaMensual(70.00)
                .build();

        mockMvc.perform(patch("/api/artes-marciales/" + idCreado)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cuotaMensual").value(70.00))
                .andExpect(jsonPath("$.nombre").value("Muay Thai Boran"));

        mockMvc.perform(delete("/api/artes-marciales/" + idCreado))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/artes-marciales/" + idCreado))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("POST con datos inválidos debe devolver HTTP 400 Bad Request con erroresCampo")
    void testPostValidacionFallida() throws Exception {
        ArteMarcialCreacionDTO invalido = ArteMarcialCreacionDTO.builder()
                .codigoFederacion("CODIGO-MAL")
                .nombre("A")
                .paisOrigen("")
                .disciplinaOlimpica(true)
                .cuotaMensual(-10.0)
                .aniosHistoria(0)
                .emailFederacion("no-es-email")
                .fechaFundacion(LocalDate.now().plusDays(10))
                .tecnicasPrincipales(List.of())
                .normativaVerificada(false)
                .build();

        mockMvc.perform(post("/api/artes-marciales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erroresCampo.codigoFederacion").exists())
                .andExpect(jsonPath("$.erroresCampo.cuotaMensual").exists())
                .andExpect(jsonPath("$.erroresCampo.emailFederacion").exists());
    }
}
