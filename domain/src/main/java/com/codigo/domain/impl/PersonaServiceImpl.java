package com.codigo.domain.impl;

import com.codigo.domain.ports.in.ServiceIn;
import com.codigo.domain.ports.out.ServiceOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PersonaServiceImpl<T,K> implements ServiceIn<T,K> {

    @Autowired(required = false)
    @Qualifier("persona_adapter")
    private ServiceOut<T,K> servicePersonaOut;

    @Override
    public T crearIn(K request) {
        return servicePersonaOut.crearOut(request);
    }

    @Override
    public T obtenerIn(Long id) {
        return servicePersonaOut.obtenerOut(id);
    }

    @Override
    public List<T> obtenerTodosIn() {
        return servicePersonaOut.obtenerTodosOut();
    }

    @Override
    public T actualizarIn(K request, Long id) {
        return servicePersonaOut.actualizarOut(request, id);
    }

    @Override
    public T deleteIn(Long id) {
        return servicePersonaOut.deleteOut(id);
    }
}
