package com.codigo.infrastructure.entity;

import com.codigo.infrastructure.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@Table(name = "persona")
@Entity
public class PersonaEntity extends BaseEntity {

    @Column(name = "nombre", nullable = false)
    private String nombres;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Column(name = "tipodocumento", nullable = false, length = 5)
    private String tipoDocumento;

    @Column(name = "numerodocumento", nullable = false, length = 20)
    private String numeroDocumento;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "telefono", length = 15)
    private String telefono;

    private String direccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private EmpresaEntity empresa;

}
