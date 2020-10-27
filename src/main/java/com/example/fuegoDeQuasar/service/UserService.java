package com.example.fuegoDeQuasar.service;

import com.example.fuegoDeQuasar.domain.User;
import com.example.fuegoDeQuasar.repository.UserRepository;
import com.example.fuegoDeQuasar.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String getToken(User user) throws Exception {
        if (!userRepository.existsByUsername(user.getUsername())) {
            user.setPassword((passwordEncoder.encode(user.getPassword())));
            User userEntity = new User();
            userEntity.setUsername(user.getUsername());
            userEntity.setPassword(user.getPassword());
            userEntity.setEmail(user.getEmail());
            userRepository.save(userEntity);
            return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        } else {
            throw new Exception("Username is already in use");
        }
    }
}