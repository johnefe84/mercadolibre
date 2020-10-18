package com.example.fuegoDeQuasar.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    @ApiModelProperty()
    private Integer id;

    @ApiModelProperty(position = 1)
    private String username;

    @ApiModelProperty(position = 2)
    private String email;
}
