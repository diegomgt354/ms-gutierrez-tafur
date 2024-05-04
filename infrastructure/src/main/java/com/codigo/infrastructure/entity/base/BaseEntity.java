package com.codigo.infrastructure.entity.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@MappedSuperclass // los campos seran heredados por un entity que lo extienda (super clase de entidades)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estado", nullable = false)
    private int estado;

    @Column(name = "usuacrea", nullable = false)
    private String usuaCreate;

    @Column(name = "datecreate", nullable = false)
    private Timestamp dateCreate;

    @Column(name = "usuamodif")
    private String usuaModif;

    @Column(name = "datemodif")
    private Timestamp dateModif;

    @Column(name = "usuadelet")
    private String usuaDelet;

    @Column(name = "datedelet")
    private Timestamp dateDelet;
}
