package com.codigo.infrastructure.dao;

import com.codigo.infrastructure.entity.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<EmpresaEntity, Long> {

    boolean existsByNumeroDocumento(String numeroDocumento);
    EmpresaEntity findByNumeroDocumento(String numeroDocumento);

}
