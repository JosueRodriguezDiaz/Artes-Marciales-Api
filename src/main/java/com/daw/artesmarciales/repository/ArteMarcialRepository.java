package com.daw.artesmarciales.repository;

import com.daw.artesmarciales.model.ArteMarcial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Capa de Persistencia (Acceso a Datos).
 */
@Repository
public interface ArteMarcialRepository extends JpaRepository<ArteMarcial, Long> {

    List<ArteMarcial> findByNombreContainingIgnoreCase(String nombre);

    List<ArteMarcial> findByPaisOrigenIgnoreCase(String paisOrigen);

    List<ArteMarcial> findByCuotaMensualLessThanEqual(Double cuotaMaxima);

    Optional<ArteMarcial> findByCodigoFederacion(String codigoFederacion);

    boolean existsByCodigoFederacion(String codigoFederacion);
}
