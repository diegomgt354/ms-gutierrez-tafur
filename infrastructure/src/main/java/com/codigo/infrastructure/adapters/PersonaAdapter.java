package com.codigo.infrastructure.adapters;

import com.codigo.domain.agregates.constants.Constants;
import com.codigo.domain.agregates.exception.BadRequestException;
import com.codigo.domain.agregates.request.PersonaRequest;
import com.codigo.domain.agregates.response.EmpresaResponse;
import com.codigo.domain.agregates.response.EmpresaSunatResponse;
import com.codigo.domain.agregates.response.PersonaReniecResponse;
import com.codigo.domain.agregates.response.PersonaResponse;
import com.codigo.domain.ports.out.ServiceOut;
import com.codigo.infrastructure.client.ClientDocument;
import com.codigo.infrastructure.dao.EmpresaRepository;
import com.codigo.infrastructure.dao.PersonaRepository;
import com.codigo.infrastructure.entity.EmpresaEntity;
import com.codigo.infrastructure.entity.PersonaEntity;
import com.codigo.infrastructure.redis.RedisService;
import com.codigo.infrastructure.utiliies.Utilities;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("persona_adapter")
@RequiredArgsConstructor
public class PersonaAdapter implements ServiceOut<PersonaResponse, PersonaRequest> {

    private final PersonaRepository personaRepository;
    private final EmpresaRepository empresaRepository;
    private final Utilities utilities;
    private final ClientDocument clientDocument;
    private final RedisService redisService;

    @Value("${api.token}")
    private String tokenDocument;

    @Override
    public PersonaResponse crearOut(PersonaRequest request) {
        boolean exist = personaRepository.existsByNumeroDocumento(request.getNumeroDocumento());
        if(exist) throw new BadRequestException("La persona ya existe en la base de datos.");
        try{
            PersonaReniecResponse response_reniec = getInfoReniec(request.getNumeroDocumento());
            PersonaEntity entity = fromReniecToEntity(response_reniec, "create", 0L);
            entity = getEntityComplete(entity,request);
            return new ModelMapper().map(personaRepository.save(entity),PersonaResponse.class);
        }catch (FeignException e){
            return null;
        }
    }

    @Override
    public PersonaResponse obtenerOut(Long id) {

        String redisValue = validateEmpresaRedis(id);

        if(redisValue != null){
            return utilities.convertStringToObject(redisValue,PersonaResponse.class);
        }

        boolean exist = personaRepository.existsById(id);
        if(!exist) throw new BadRequestException("La persona no existe en la base de datos.");

        PersonaEntity entity = personaRepository.findById(id).orElse(null);
        PersonaResponse response = new ModelMapper().map(entity, PersonaResponse.class);
        saveEmpresaRedis(response, id);
        return response;
    }

    @Override
    public List<PersonaResponse> obtenerTodosOut() {
        List<PersonaEntity> personas = personaRepository.findAll();
        return personas.stream().map(p->new ModelMapper().map(p, PersonaResponse.class)).toList();
    }

    @Override
    public PersonaResponse actualizarOut(PersonaRequest request, Long id) {
        boolean exist = personaRepository.existsById(id);
        if(!exist) throw new BadRequestException("La persona no existe en la base de datos.");
        try{
            PersonaReniecResponse response_reniec = getInfoReniec(request.getNumeroDocumento());
            PersonaEntity entity = fromReniecToEntity(response_reniec, "update", id);
            entity = getEntityComplete(entity,request);
            PersonaResponse response = new ModelMapper().map(personaRepository.save(entity), PersonaResponse.class);
            saveEmpresaRedis(response, id);
            return response;
        }catch (FeignException e){
            return null;
        }
    }

    @Override
    public PersonaResponse deleteOut(Long id) {
        boolean exist = personaRepository.existsById(id);
        if(!exist) throw new BadRequestException("La persona no existe en la base de datos.");
        PersonaEntity entity_before = personaRepository.findById(id).orElse(null);
        try{
            PersonaReniecResponse response_sunat = getInfoReniec(entity_before.getNumeroDocumento());
            PersonaEntity entity = fromReniecToEntity(response_sunat, "delete", id);
            PersonaResponse response = new ModelMapper().map(personaRepository.save(entity), PersonaResponse.class);
            saveEmpresaRedis(response, id);
            return response;
        }catch (FeignException e){
            return null;
        }
    }

    public PersonaEntity getEntityComplete(PersonaEntity entity, PersonaRequest request){
        boolean empresaExist = empresaRepository.existsByNumeroDocumento(request.getEmpresa());
        if(!empresaExist) throw new BadRequestException("La empresa no existe en la base de datos.");
        new ModelMapper().map(request,entity);
        EmpresaEntity empresaEntity = empresaRepository.findByNumeroDocumento(request.getEmpresa());
        entity.setEmpresa(empresaEntity);
        return entity;
    }

    public PersonaEntity fromReniecToEntity(PersonaReniecResponse response, String accion, Long id){
        PersonaEntity entity;
        if(accion.equals("create")){
            entity = new PersonaEntity();
            new ModelMapper().map(response, entity);
            entity.setEstado(Constants.STATUS_ACTIVE);
            entity.setUsuaCreate(Constants.AUDIT_ADMIN);
            entity.setDateCreate(utilities.getTimestampToday());
        }else{
            entity = personaRepository.findById(id).orElse(null);
            if (entity==null) throw new BadRequestException("La persona no existe en la base de datos.");
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


    public PersonaReniecResponse getInfoReniec(String documento){
        return clientDocument.getInfoReniec(documento, tokenDocument);
    }

    public String validateEmpresaRedis(Long id){
        return redisService.getRedisValue(Constants.REDIS_PERSONA_KEY+id);
    }

    public void saveEmpresaRedis(PersonaResponse entity, Long id){
        String redisData = utilities.convertObjectToString(entity);
        redisService.saveRedisIn(Constants.REDIS_PERSONA_KEY+id, redisData, 10);
    }
}
