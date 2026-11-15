package com.daw.artesmarciales.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

/**
 * Capa de Configuración: Define los Beans globales de infraestructura.
 * Configura el cliente HTTP síncrono (RestClient) con tiempos de espera (timeouts) explícitos
 * para evitar bloqueos si la API externa tarda demasiado en responder.
 */
@Configuration
@EnableCaching
public class RestClientConfig {

    /**
     * Crea y registra en el contenedor de Spring un Bean de tipo RestClient.
     * Se inyectará en ExternalApiService para realizar peticiones HTTP a APIs de terceros.
     *
     * @return Instancia configurada de RestClient con timeouts de conexión y lectura.
     */
    @Bean
    public RestClient restClient() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        requestFactory.setReadTimeout(Duration.ofSeconds(5));

        return RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }
}
