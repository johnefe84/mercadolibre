package com.example.fuegoDeQuasar.domain;

import com.example.fuegoDeQuasar.enums.SatelitteEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Satellite {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "nombre")
    @Enumerated(EnumType.STRING)
    private SatelitteEnum nombre;

    @Column(name = "distancia")
    private float distancia;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "satellite_id" , insertable = false, updatable = false)
    private List<Messages> messagesList;
}