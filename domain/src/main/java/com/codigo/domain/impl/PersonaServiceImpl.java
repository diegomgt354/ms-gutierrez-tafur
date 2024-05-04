package com.codigo.domain.impl;

import com.codigo.domain.agregates.request.PersonaRequest;
import com.codigo.domain.agregates.response.PersonaResponse;
import com.codigo.domain.ports.in.PersonaServiceIn;
import com.codigo.domain.ports.in.base.BaseServiceIn;
import com.codigo.domain.ports.out.PersonaServiceOut;
import com.codigo.domain.ports.out.base.BaseServiceOut;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaServiceIn {

    private final PersonaServiceOut servicePersonaOut;

    @Override
    public PersonaResponse crearIn(PersonaRequest request) {
        return servicePersonaOut.crearOut(request);
    }

    @Override
    public PersonaResponse obtenerIn(Long id) {
        return servicePersonaOut.obtenerOut(id);
    }

    @Override
    public List<PersonaResponse> obtenerTodosIn() {
        return servicePersonaOut.obtenerTodosOut();
    }

    @Override
    public PersonaResponse actualizarIn(PersonaRequest request, Long id) {
        return servicePersonaOut.actualizarOut(request, id);
    }

    @Override
    public PersonaResponse deleteIn(Long id) {
        return servicePersonaOut.deleteOut(id);
    }
}
