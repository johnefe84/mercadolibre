package com.example.fuegoDeQuasar.service;

import com.example.fuegoDeQuasar.domain.User;
import com.example.fuegoDeQuasar.dto.UserDataDTO;
import com.example.fuegoDeQuasar.enums.Role;
import com.example.fuegoDeQuasar.repository.UserRepository;
import com.example.fuegoDeQuasar.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void getTokenTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        String userName = "user";
        String password = "password";
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZWxpIiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9DTElFTlQifV0sImlhdCI6MTYwMjk5NjYzNCwiZXhwIjoxNjAyOTk2OTM0fQ.Qzs-Wu4ewuSBPI7p_WbHr7RfiwL7ZE2MbVl-paviqYw";
        String email = "user@mercadolibre.com";
        List<Role> roles = new ArrayList<>();
        roles.add(Role.valueOf("ROLE_CLIENT"));

        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setEmail(email);
        userDataDTO.setPassword(password);
        userDataDTO.setUsername(userName);
        userDataDTO.setRoles(roles);

        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userDataDTO, User.class);

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtTokenProvider.createToken(user.getUsername(), user.getRoles())).thenReturn(token);

        String result = userService.getToken(user);

        verify(jwtTokenProvider, times(1)).createToken(user.getUsername(), user.getRoles());
        verify(userRepository, times(1)).existsByUsername(user.getUsername());
        verify(userRepository, times(1)).save(any(User.class));

        assertNotNull(Objects.requireNonNull(result));
        assertEquals(token, result);
    }

    @Test
    public void getTokenUserInUseTest() {
        MockitoAnnotations.initMocks(this);
        String userName = "user";
        String password = "password";
        String email = "user@mercadolibre.com";
        List<Role> roles = new ArrayList<>();
        roles.add(Role.valueOf("ROLE_CLIENT"));

        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setEmail(email);
        userDataDTO.setPassword(password);
        userDataDTO.setUsername(userName);
        userDataDTO.setRoles(roles);

        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userDataDTO, User.class);

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        assertThrows(Exception.class, ()->{ userService.getToken(user);} );
    }
}
