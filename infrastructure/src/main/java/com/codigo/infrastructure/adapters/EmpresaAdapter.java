package com.codigo.infrastructure.adapters;

import com.codigo.domain.agregates.constants.Constants;
import com.codigo.domain.agregates.exception.BadRequestException;
import com.codigo.domain.agregates.request.EmpresaRequest;
import com.codigo.domain.agregates.response.EmpresaResponse;
import com.codigo.domain.agregates.response.EmpresaSunatResponse;
import com.codigo.domain.ports.out.ServiceOut;
import com.codigo.infrastructure.client.ClientDocument;
import com.codigo.infrastructure.dao.EmpresaRepository;
import com.codigo.infrastructure.entity.EmpresaEntity;
import com.codigo.infrastructure.redis.RedisService;
import com.codigo.infrastructure.utiliies.Utilities;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("empresa_adapter")
@RequiredArgsConstructor
public class EmpresaAdapter implements ServiceOut<EmpresaResponse, EmpresaRequest> {

    private final EmpresaRepository empresaRepository;
    private final Utilities utilities;
    private final ClientDocument clientDocument;
    private final RedisService redisService;

    @Value("${api.token}")
    private String tokenDocument;

    @Override
    public EmpresaResponse crearOut(EmpresaRequest request) {
        boolean exits = empresaRepository.existsByNumeroDocumento(request.getNumeroDocumento());
        if (exits) throw new BadRequestException("La empresa ya existe en la base de datos.");
        try{
            EmpresaSunatResponse response_sunat = getInfoSunat(request.getNumeroDocumento());
            EmpresaEntity entity = fromSunatToEntity(response_sunat, "create",0L);
            return new ModelMapper().map(empresaRepository.save(entity), EmpresaResponse.class);
        }catch (FeignException e){
            throw new BadRequestException("RUC incorrecto");
        }
    }

    @Override
    public EmpresaResponse obtenerOut(Long id) {

        String redisValue = validateEmpresaRedis(id);

        if(redisValue != null){
            return utilities.convertStringToObject(redisValue,EmpresaResponse.class);
        }

        boolean exist = empresaRepository.existsById(id);
        if(!exist) throw new BadRequestException("La empresa no existe en la base de datos.");

        EmpresaEntity entity = empresaRepository.findById(id).orElse(null);
        EmpresaResponse response = new ModelMapper().map(entity, EmpresaResponse.class);
        saveEmpresaRedis(response, id);
        return response;
    }

    @Override
    public List<EmpresaResponse> obtenerTodosOut() {
        List<EmpresaEntity> empresas = empresaRepository.findAll();
        return empresas.stream().map(e-> new ModelMapper().map(e,EmpresaResponse.class)).toList();
    }

    @Override
    public EmpresaResponse actualizarOut(EmpresaRequest request, Long id) {
        boolean exist = empresaRepository.existsById(id);
        if(!exist) throw new BadRequestException("La empresa no existe en la base de datos.");
        try{
            EmpresaSunatResponse response_sunat = getInfoSunat(request.getNumeroDocumento());
            EmpresaEntity entity = fromSunatToEntity(response_sunat, "update", id);
            EmpresaResponse response = new ModelMapper().map(empresaRepository.save(entity), EmpresaResponse.class);
            saveEmpresaRedis(response, id);
            return response;
        }catch (FeignException e){
            return null;
        }
    }

    @Override
    public EmpresaResponse deleteOut(Long id) {
        boolean exist = empresaRepository.existsById(id);
        if(!exist) throw new BadRequestException("La empresa no existe en la base de datos.");
        EmpresaEntity entity_before = empresaRepository.findById(id).orElse(null);
        try{
            EmpresaSunatResponse response_sunat = getInfoSunat(entity_before.getNumeroDocumento());
            EmpresaEntity entity = fromSunatToEntity(response_sunat, "delete", id);
            EmpresaResponse response = new ModelMapper().map(empresaRepository.save(entity), EmpresaResponse.class);
            saveEmpresaRedis(response, id);
            return response;
        }catch (FeignException e){
            return null;
        }
    }

    public EmpresaEntity fromSunatToEntity(EmpresaSunatResponse response, String accion, Long id){
        EmpresaEntity entity;
        if(accion.equals("create")){
            entity = new EmpresaEntity();
            new ModelMapper().map(response, entity);
            entity.setEstado(Constants.STATUS_ACTIVE);
            entity.setUsuaCreate(Constants.AUDIT_ADMIN);
            entity.setDateCreate(utilities.getTimestampToday());
        }else{
            entity = empresaRepository.findById(id).orElse(null);
            if (entity==null) throw new BadRequestException("La empresa no existe en la base de datos.");
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
        return clientDocument.getInfoSunat(documento, tokenDocument);
    }


    public String validateEmpresaRedis(Long id){
        return redisService.getRedisValue(Constants.REDIS_EMPRESA_KEY+id);
    }

    public void saveEmpresaRedis(EmpresaResponse entity, Long id){
        String redisData = utilities.convertObjectToString(entity);
        redisService.saveRedisIn(Constants.REDIS_EMPRESA_KEY+id, redisData, 10);
    }
}
