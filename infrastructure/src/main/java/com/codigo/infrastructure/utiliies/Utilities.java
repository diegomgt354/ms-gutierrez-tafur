package com.codigo.infrastructure.utiliies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class Utilities {

    // TODO: retorna la fecha y hora actual
    public Timestamp getTimestampToday(){
        long currentTime = System.currentTimeMillis();
        return new Timestamp(currentTime);
    }

    // TODO: convertir un objeto a String
    public <T> String convertObjectToString(T objeto){
        try {
            return new ObjectMapper().writeValueAsString(objeto);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    // TODO: convertir un string a un objeto
    public <T> T convertStringToObject(String json, Class<T> tipoClase){
        try {
            return new ObjectMapper().readValue(json,tipoClase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
