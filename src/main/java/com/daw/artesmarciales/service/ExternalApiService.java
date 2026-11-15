package com.daw.artesmarciales.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.text.Normalizer;
import java.util.Map;

/**
 * Servicio dedicado a la integración síncrona con una API pública externa (REST Countries).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalApiService {

    private final RestClient restClient;

    private static final Map<String, String> TRADUCCION_PAISES = Map.ofEntries(
            Map.entry("japon", "japan"),
            Map.entry("corea del sur", "south korea"),
            Map.entry("brasil", "brazil"),
            Map.entry("china", "china"),
            Map.entry("tailandia", "thailand"),
            Map.entry("espana", "spain"),
            Map.entry("estados unidos", "united states"),
            Map.entry("francia", "france"),
            Map.entry("rusia", "russia"),
            Map.entry("israel", "israel"),
            Map.entry("filipinas", "philippines")
    );

    public record DatosPaisExterno(
            String capital,
            String region,
            Long poblacion,
            String banderaUrl,
            String origen
    ) {}

    public DatosPaisExterno obtenerDatosPais(String paisOrigen) {
        try {
            String paisNormalizado = normalizarTexto(paisOrigen);
            String nombreApi = TRADUCCION_PAISES.getOrDefault(paisNormalizado, paisNormalizado);

            String url = "https://restcountries.com/v3.1/name/" + nombreApi + "?fullText=true";
            log.info("Consultando API externa REST Countries: {}", url);

            JsonNode respuesta = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(JsonNode.class);

            if (respuesta != null && respuesta.isArray() && !respuesta.isEmpty()) {
                JsonNode paisNode = respuesta.get(0);

                String capital = paisNode.has("capital") && paisNode.get("capital").isArray()
                        ? paisNode.get("capital").get(0).asText("Desconocida")
                        : "Desconocida";

                String region = paisNode.has("region")
                        ? paisNode.get("region").asText("Desconocida")
                        : "Desconocida";

                Long poblacion = paisNode.has("population")
                        ? paisNode.get("population").asLong(0L)
                        : 0L;

                String bandera = paisNode.has("flags") && paisNode.get("flags").has("png")
                        ? paisNode.get("flags").get("png").asText("Sin bandera")
                        : "Sin bandera";

                return new DatosPaisExterno(
                        capital,
                        region,
                        poblacion,
                        bandera,
                        "API Externa REST Countries (En vivo)"
                );
            }
        } catch (Exception ex) {
            log.warn("Fallo al contactar con REST Countries para '{}'. Activando Fallback. Motivo: {}",
                    paisOrigen, ex.getMessage());
        }

        return new DatosPaisExterno(
                "No disponible (Modo Offline)",
                "Región no disponible",
                0L,
                "https://via.placeholder.com/320x200?text=Sin+Conexion",
                "Fallback Local (Servicio externo no disponible)"
        );
    }

    /**
     * Elimina tildes y convierte a minúsculas para facilitar la búsqueda en el mapa de traducción.
     */
    private String normalizarTexto(String texto) {
        if (texto == null) return "";
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return sinTildes.toLowerCase().trim();
    }
}
