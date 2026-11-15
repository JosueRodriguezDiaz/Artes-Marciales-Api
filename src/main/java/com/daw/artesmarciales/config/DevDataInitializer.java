package com.daw.artesmarciales.config;

import com.daw.artesmarciales.model.ArteMarcial;
import com.daw.artesmarciales.repository.ArteMarcialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Inicializador de datos de prueba para el entorno de desarrollo.
 * @Profile("dev"): Asegura que este componente SOLO se ejecute cuando el perfil activo sea 'dev'.
 * CommandLineRunner: Ejecuta el método run() automáticamente justo después de arrancar Spring Boot.
 */
@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DevDataInitializer implements CommandLineRunner {

    private final ArteMarcialRepository repository;

    /**
     * Precarga registros iniciales de artes marciales en la base de datos H2 en memoria
     * si la tabla se encuentra vacía.
     */
    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            log.info(">>> [PERFIL DEV] Precargando catálogo inicial de Artes Marciales en H2...");

            ArteMarcial karate = ArteMarcial.builder()
                    .codigoFederacion("AM-JP-001")
                    .nombre("Karate Shotokan")
                    .paisOrigen("Japon")
                    .disciplinaOlimpica(true)
                    .cuotaMensual(55.50)
                    .aniosHistoria(102)
                    .emailFederacion("contacto@karateshotokan.jp")
                    .fechaFundacion(LocalDate.of(1922, 5, 1))
                    .tecnicasPrincipales(List.of("Mae Geri", "Gyaku Zuki", "Kata Heian Shodan"))
                    .build();

            ArteMarcial judo = ArteMarcial.builder()
                    .codigoFederacion("AM-JP-002")
                    .nombre("Judo Kodokan")
                    .paisOrigen("Japon")
                    .disciplinaOlimpica(true)
                    .cuotaMensual(60.00)
                    .aniosHistoria(144)
                    .emailFederacion("info@kodokan-judo.org")
                    .fechaFundacion(LocalDate.of(1882, 2, 1))
                    .tecnicasPrincipales(List.of("Ippon Seoi Nage", "O Soto Gari", "Uchi Mata"))
                    .build();

            ArteMarcial taekwondo = ArteMarcial.builder()
                    .codigoFederacion("AM-KR-003")
                    .nombre("Taekwondo WT")
                    .paisOrigen("Corea del Sur")
                    .disciplinaOlimpica(true)
                    .cuotaMensual(50.00)
                    .aniosHistoria(71)
                    .emailFederacion("admin@worldtaekwondo.kr")
                    .fechaFundacion(LocalDate.of(1955, 4, 11))
                    .tecnicasPrincipales(List.of("Dollyo Chagi", "Yop Chagi", "Ap Chagi"))
                    .build();

            ArteMarcial bjj = ArteMarcial.builder()
                    .codigoFederacion("AM-BR-004")
                    .nombre("Brazilian Jiu-Jitsu")
                    .paisOrigen("Brasil")
                    .disciplinaOlimpica(false)
                    .cuotaMensual(75.00)
                    .aniosHistoria(101)
                    .emailFederacion("federacion@ibjjf-brasil.br")
                    .fechaFundacion(LocalDate.of(1925, 10, 1))
                    .tecnicasPrincipales(List.of("Armbar", "Triangle Choke", "Rear Naked Choke"))
                    .build();

            repository.saveAll(List.of(karate, judo, taekwondo, bjj));
            log.info(">>> [PERFIL DEV] Se han insertado {} artes marciales correctamente.", repository.count());
        }
    }
}
