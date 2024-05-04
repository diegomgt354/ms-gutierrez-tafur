package com.codigo.infrastructure.entity;

import com.codigo.infrastructure.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@Table(name = "empresa_info")
@Entity
public class EmpresaEntity extends BaseEntity implements Serializable {

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

}
