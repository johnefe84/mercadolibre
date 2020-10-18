package com.example.fuegoDeQuasar.repository;

import com.example.fuegoDeQuasar.domain.Satellite;
import com.example.fuegoDeQuasar.enums.SatelitteEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SatelliteRepository extends JpaRepository<Satellite, Integer> {

    Optional<Satellite> findByNombre(SatelitteEnum name);
}
