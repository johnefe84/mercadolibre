package com.example.fuegoDeQuasar.config;

import com.example.fuegoDeQuasar.dto.MessageDTO;
import com.example.fuegoDeQuasar.exception.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.example.fuegoDeQuasar.util.Constants.FAILURE;

@ControllerAdvice
public class ExceptionManager {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDTO> defaultErrorHandler(Exception e) {
        MessageDTO responseDTO = new MessageDTO(null,FAILURE+ e.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<MessageDTO> jwtErrorHandler(Exception e) {
        MessageDTO responseDTO = new MessageDTO(null,FAILURE+ e.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.FORBIDDEN);
    }
}
