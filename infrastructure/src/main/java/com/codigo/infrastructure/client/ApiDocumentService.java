package com.codigo.infrastructure.client;

import com.codigo.domain.agregates.constants.Constants;
import com.codigo.domain.agregates.exception.BadRequestException;
import com.codigo.domain.agregates.response.EmpresaSunatResponse;
import com.codigo.domain.agregates.response.PersonaReniecResponse;
import com.codigo.infrastructure.entity.base.BaseEntity;
import com.codigo.infrastructure.utiliies.Utilities;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiDocumentService {

    private final ClientReniec clientReniec;
    private final ClientSunat clientSunat;
    private final Utilities utilities;

    @Value("${api.token}")
    private String tokenDocument;


    public <T extends BaseEntity,R> T fromDocumentToEntity
            (T entity,
             JpaRepository<T,Long> repository,
             R response,
             String accion,
             Long id) {
        if(accion.equals("create")){
            new ModelMapper().map(response, entity);
            entity.setEstado(Constants.STATUS_ACTIVE);
            entity.setUsuaCreate(Constants.AUDIT_ADMIN);
            entity.setDateCreate(utilities.getTimestampToday());
        }else{
            entity = repository.findById(id).orElse(null);
            if (entity==null) throw new BadRequestException("Entidad no existe en la base de datos.");
            new ModelMapper().map(response, entity);
            if (accion.equals("update")){
                entity.setUsuaModif(Constants.AUDIT_ADMIN);
                entity.setDateModif(utilities.getTimestampToday());
            } else if (accion.equals("delete")) {
                entity.setEstado(Constants.STATUS_INACTIVE);
                entity.setUsuaDelet(Constants.AUDIT_ADMIN);
                entity.setDateDelet(utilities.getTimestampToday());
            }
        }
        return entity;
    }

    public EmpresaSunatResponse getInfoSunat(String documento){
        return clientSunat.getInfoSunat(documento, tokenDocument);
    }

    public PersonaReniecResponse getInfoReniec(String documento){
        return clientReniec.getInfoReniec(documento, tokenDocument);
    }
}
