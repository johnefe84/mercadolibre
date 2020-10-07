package com.example.fuegoDeQuasar.repository;

import com.example.fuegoDeQuasar.domain.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, Integer> {
    List<Messages> findDistinctByIdSatellite(Integer satId);

    //Optional<Messages> findByIdSatelliteAndMessage(Integer satId, String message);
}
