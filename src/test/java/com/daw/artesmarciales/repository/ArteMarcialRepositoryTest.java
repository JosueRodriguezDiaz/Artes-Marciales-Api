package com.daw.artesmarciales.repository;

import com.daw.artesmarciales.model.ArteMarcial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ArteMarcialRepositoryTest {

    @Autowired
    private ArteMarcialRepository repository;

    @Test
    @DisplayName("Debe encontrar artes marciales por coincidencia parcial en el nombre ignorando mayúsculas")
    void testFindByNombreContainingIgnoreCase() {
        ArteMarcial aikido = ArteMarcial.builder()
                .codigoFederacion("AM-JP-010")
                .nombre("Aikido Tradicional")
                .paisOrigen("Japon")
                .disciplinaOlimpica(false)
                .cuotaMensual(50.0)
                .aniosHistoria(95)
                .emailFederacion("info@aikido.jp")
                .fechaFundacion(LocalDate.of(1931, 1, 1))
                .tecnicasPrincipales(List.of("Irimi Nage", "Kotegaeshi"))
                .build();
        repository.save(aikido);

        List<ArteMarcial> encontrados = repository.findByNombreContainingIgnoreCase("aikido");

        assertThat(encontrados).hasSize(1);
        assertThat(encontrados.get(0).getCodigoFederacion()).isEqualTo("AM-JP-010");
        assertThat(encontrados.get(0).getTecnicasPrincipales()).contains("Irimi Nage");
    }

    @Test
    @DisplayName("Debe filtrar artes marciales por cuota mensual máxima")
    void testFindByCuotaMensualLessThanEqual() {
        ArteMarcial barato = ArteMarcial.builder()
                .codigoFederacion("AM-CN-011")
                .nombre("Kung Fu Wing Chun")
                .paisOrigen("China")
                .disciplinaOlimpica(false)
                .cuotaMensual(40.0)
                .aniosHistoria(200)
                .emailFederacion("wingchun@kungfu.cn")
                .fechaFundacion(LocalDate.of(1820, 1, 1))
                .tecnicasPrincipales(List.of("Pak Sao", "Tan Sao"))
                .build();
        repository.save(barato);

        List<ArteMarcial> resultado = repository.findByCuotaMensualLessThanEqual(45.0);
        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getNombre()).isEqualTo("Kung Fu Wing Chun");
    }
}
