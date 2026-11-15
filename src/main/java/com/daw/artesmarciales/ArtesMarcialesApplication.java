package com.daw.artesmarciales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Clase principal de arranque de la aplicación Spring Boot.
 * @SpringBootApplication: Habilita la autoconfiguración, el escaneo de componentes y la configuración de Spring.
 * @EnableCaching: Activa el soporte de gestión de caché en memoria RAM en toda la aplicación.
 */
@SpringBootApplication
@EnableCaching
public class ArtesMarcialesApplication {

    /**
     * Punto de entrada principal de la máquina virtual de Java (JVM).
     * Arranca el servidor embebido Tomcat y levanta el contexto de Spring.
     */
    public static void main(String[] args) {
        SpringApplication.run(ArtesMarcialesApplication.class, args);
    }
}
