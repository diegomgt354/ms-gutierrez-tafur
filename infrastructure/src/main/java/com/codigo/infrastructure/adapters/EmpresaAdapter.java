package com.codigo.infrastructure.adapters;

import com.codigo.domain.agregates.constants.Constants;
import com.codigo.domain.agregates.exception.BadRequestException;
import com.codigo.domain.agregates.request.EmpresaRequest;
import com.codigo.domain.agregates.response.EmpresaResponse;
import com.codigo.domain.agregates.response.EmpresaSunatResponse;
import com.codigo.domain.ports.out.EmpresaServiceOut;
import com.codigo.infrastructure.client.ClientReniec;
import com.codigo.infrastructure.client.ApiDocumentService;
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

@Service
@RequiredArgsConstructor
public class EmpresaAdapter implements EmpresaServiceOut {

    private final EmpresaRepository empresaRepository;
    private final Utilities utilities;
    private final RedisService redisService;

    private final ApiDocumentService apiDocumentService;

    @Value("${api.token}")
    private String tokenDocument;

    @Override
    public EmpresaResponse crearOut(EmpresaRequest request) {
        boolean exits = empresaRepository.existsByNumeroDocumento(request.getNumeroDocumento());
        if (exits) throw new BadRequestException("La empresa ya existe en la base de datos.");
        try{
            EmpresaSunatResponse response_sunat = apiDocumentService.getInfoSunat(request.getNumeroDocumento());
            EmpresaEntity entity = apiDocumentService.fromDocumentToEntity(new EmpresaEntity(),empresaRepository,response_sunat, "create", 0L);

            return new ModelMapper().map(empresaRepository.save(entity), EmpresaResponse.class);
        }catch (FeignException e){
            throw new BadRequestException("RUC incorrecto");
        }
    }

    @Override
    public EmpresaResponse obtenerOut(Long id) {

        String redisValue = redisService.validateObjectInRedis(Constants.REDIS_EMPRESA_KEY+id);

        if(redisValue != null){
            return utilities.convertStringToObject(redisValue,EmpresaResponse.class);
        }

        boolean exist = empresaRepository.existsById(id);
        if(!exist) throw new BadRequestException("La empresa no existe en la base de datos.");

        EmpresaEntity entity = empresaRepository.findById(id).orElse(null);
        EmpresaResponse response = new ModelMapper().map(entity, EmpresaResponse.class);
        redisService.saveObjectInRedis(response, Constants.REDIS_EMPRESA_KEY+id);
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
            EmpresaSunatResponse response_sunat = apiDocumentService.getInfoSunat(request.getNumeroDocumento());
            EmpresaEntity entity = apiDocumentService.fromDocumentToEntity(new EmpresaEntity(),empresaRepository,response_sunat, "update", id);
            EmpresaResponse response = new ModelMapper().map(empresaRepository.save(entity), EmpresaResponse.class);
            redisService.saveObjectInRedis(response, Constants.REDIS_EMPRESA_KEY+id);
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
            EmpresaSunatResponse response_sunat = apiDocumentService.getInfoSunat(entity_before.getNumeroDocumento());
            EmpresaEntity entity = apiDocumentService.fromDocumentToEntity(new EmpresaEntity(),empresaRepository,response_sunat, "delete", id);
            EmpresaResponse response = new ModelMapper().map(empresaRepository.save(entity), EmpresaResponse.class);
            redisService.saveObjectInRedis(response, Constants.REDIS_EMPRESA_KEY+id);
            return response;
        }catch (FeignException e){
            return null;
        }
    }

}
