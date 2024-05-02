package com.codigo.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@Table(name = "empresa_info")
@Entity
public class EmpresaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razonsocial", nullable = false)
    private String razonSocial;

    @Column(name = "tipodocumento", length = 5, nullable = false)
    private String tipoDocumento;

    @Column(name = "numerodocumento", length = 20, nullable = false, unique = true)
    private String numeroDocumento;

    @Column(name = "condicion", length = 50, nullable = false)
    private String condicion;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "distrito", length = 100)
    private String distrito;

    @Column(name = "provincia", length = 100)
    private String provincia;

    @Column(name = "departamento", length = 100)
    private String departamento;

    @Column(name = "esagenteretencion", nullable = false)
    private boolean EsAgenteRetencion = false;

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
