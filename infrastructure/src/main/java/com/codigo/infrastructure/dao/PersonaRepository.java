package com.codigo.infrastructure.dao;

import com.codigo.infrastructure.entity.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<PersonaEntity, Long> {

    boolean existsByNumeroDocumento(String numeroDocumento);
}
