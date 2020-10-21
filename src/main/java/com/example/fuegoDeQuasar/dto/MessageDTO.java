package com.example.fuegoDeQuasar.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MessageDTO {
    private PositionDTO position;
    private String message;
}
