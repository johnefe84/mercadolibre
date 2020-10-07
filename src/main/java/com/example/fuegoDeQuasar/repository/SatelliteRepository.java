package com.example.fuegoDeQuasar.repository;

import com.example.fuegoDeQuasar.domain.Satellite;
import com.example.fuegoDeQuasar.enums.SatelitteEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SatelliteRepository extends JpaRepository<Satellite, Integer> {

    Optional<Satellite> findByNombre(SatelitteEnum name);
}
