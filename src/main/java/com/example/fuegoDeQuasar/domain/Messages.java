package com.example.fuegoDeQuasar.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Messages {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "satellite_id")
    private Integer idSatellite;

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "satellite_id", insertable = false,updatable = false)
    private Satellite satellite;
}