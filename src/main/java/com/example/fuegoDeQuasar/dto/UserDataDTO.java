package com.example.fuegoDeQuasar.dto;

import com.example.fuegoDeQuasar.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDataDTO {
    private String username;
    private String email;
    private String password;
    List<Role> roles;
}
