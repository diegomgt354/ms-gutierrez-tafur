package com.codigo.infrastructure.adapters;

import com.codigo.domain.agregates.constants.Constants;
import com.codigo.domain.agregates.exception.BadRequestException;
import com.codigo.domain.agregates.request.PersonaRequest;
import com.codigo.domain.agregates.response.PersonaReniecResponse;
import com.codigo.domain.agregates.response.PersonaResponse;
import com.codigo.domain.ports.out.PersonaServiceOut;
import com.codigo.infrastructure.client.ApiDocumentService;
import com.codigo.infrastructure.dao.EmpresaRepository;
import com.codigo.infrastructure.dao.PersonaRepository;
import com.codigo.infrastructure.entity.EmpresaEntity;
import com.codigo.infrastructure.entity.PersonaEntity;
import com.codigo.infrastructure.redis.RedisService;
import com.codigo.infrastructure.utiliies.Utilities;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonaAdapter implements PersonaServiceOut {

    private final PersonaRepository personaRepository;
    private final EmpresaRepository empresaRepository;
    private final Utilities utilities;
    private final RedisService redisService;
    private final ApiDocumentService apiDocumentService;

    @Override
    public PersonaResponse crearOut(PersonaRequest request) {
        boolean exist = personaRepository.existsByNumeroDocumento(request.getNumeroDocumento());
        if(exist) throw new BadRequestException("La persona ya existe en la base de datos.");
        try{
            PersonaReniecResponse response_reniec = apiDocumentService.getInfoReniec(request.getNumeroDocumento());
            PersonaEntity entity = apiDocumentService.fromDocumentToEntity(new PersonaEntity(),personaRepository,response_reniec, "create", 0L);
            entity = getEntityComplete(entity,request);
            return new ModelMapper().map(personaRepository.save(entity),PersonaResponse.class);
        }catch (FeignException e){
            return null;
        }
    }

    @Override
    public PersonaResponse obtenerOut(Long id) {
        String redisValue = redisService.validateObjectInRedis(Constants.REDIS_PERSONA_KEY+id);
        if(redisValue != null) return utilities.convertStringToObject(redisValue,PersonaResponse.class);
        boolean exist = personaRepository.existsById(id);
        if(!exist) throw new BadRequestException("La persona no existe en la base de datos.");
        PersonaEntity entity = personaRepository.findById(id).orElse(null);
        PersonaResponse response = new ModelMapper().map(entity, PersonaResponse.class);
        redisService.saveObjectInRedis(response, Constants.REDIS_PERSONA_KEY+id);
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
            PersonaReniecResponse response_reniec = apiDocumentService.getInfoReniec(request.getNumeroDocumento());
            PersonaEntity entity = apiDocumentService.fromDocumentToEntity(new PersonaEntity(),personaRepository,
                    response_reniec, "update", id);
            entity = getEntityComplete(entity,request);
            PersonaResponse response = new ModelMapper().map(personaRepository.save(entity), PersonaResponse.class);
            redisService.saveObjectInRedis(response, Constants.REDIS_PERSONA_KEY+id);
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
            PersonaReniecResponse response_reniec = apiDocumentService.getInfoReniec(entity_before.getNumeroDocumento());
            PersonaEntity entity = apiDocumentService.fromDocumentToEntity(new PersonaEntity(),personaRepository,
                    response_reniec, "delete", id);
            PersonaResponse response = new ModelMapper().map(personaRepository.save(entity), PersonaResponse.class);
            redisService.saveObjectInRedis(response, Constants.REDIS_PERSONA_KEY+id);
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

}
