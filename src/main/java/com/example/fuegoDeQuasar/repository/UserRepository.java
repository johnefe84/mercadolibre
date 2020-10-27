package com.example.fuegoDeQuasar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.fuegoDeQuasar.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);

    User findByUsername(String username);
}